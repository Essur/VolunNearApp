package com.volunnear.services.activities;

import com.volunnear.dtos.ActivityNotificationDTO;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteerInActivity;
import com.volunnear.entitiy.infos.OrganisationInfo;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.events.ActivityCreationEvent;
import com.volunnear.exceptions.AuthErrorException;
import com.volunnear.repositories.infos.ActivitiesRepository;
import com.volunnear.repositories.infos.VolunteersInActivityRepository;
import com.volunnear.services.users.OrganisationService;
import com.volunnear.services.users.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final UserService userService;
    private final OrganisationService organisationService;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivitiesRepository activitiesRepository;
    private final VolunteersInActivityRepository volunteersInActivityRepository;

    public ResponseEntity<?> addActivityToOrganisation(AddActivityRequestDTO activityRequest, Principal principal) {
        Optional<AppUser> organisation = userService.findAppUserByUsername(principal.getName());
        if (organisation.isEmpty()) {
            return new ResponseEntity<>(new AuthErrorException(HttpStatus.UNAUTHORIZED.value(), "Incorrect token data about organisation"), HttpStatus.UNAUTHORIZED);
        }
        if (!organisationService.isUserAreOrganisation(organisation.get())) {
            return new ResponseEntity<>("Bad try, you are not organisation", HttpStatus.BAD_REQUEST);
        }
        Activity activity = new Activity();

        activity.setTitle(activityRequest.getTitle());
        activity.setDescription(activityRequest.getDescription());
        activity.setCountry(activityRequest.getCountry());
        activity.setCity(activityRequest.getCity());
        activity.setDateOfPlace(new Date());
        activity.setKindOfActivity(activityRequest.getKindOfActivity());
        activity.setAppUser(organisation.get());
        activitiesRepository.save(activity);

        sendNotificationForSubscribers(activity, "New");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Async
    public void sendNotificationForSubscribers(Activity activity, String status) {
        ActivityNotificationDTO notificationDTO = new ActivityNotificationDTO();
        ActivityDTO activityDTO = new ActivityDTO();

        activityDTO.setCity(activity.getCity());
        activityDTO.setCountry(activity.getCountry());
        activityDTO.setKindOfActivity(activity.getKindOfActivity());
        activityDTO.setTitle(activity.getTitle());
        activityDTO.setDescription(activity.getDescription());
        activityDTO.setDateOfPlace(activity.getDateOfPlace());

        notificationDTO.setActivityDTO(activityDTO);
        notificationDTO.setOrganisationResponseDTO(getOrganisationResponseDTO(organisationService.findAdditionalInfoAboutOrganisation(activity.getAppUser())));
        eventPublisher.publishEvent(new ActivityCreationEvent(this, notificationDTO, status));
    }

    public ResponseEntity<?> getAllActivitiesOfAllOrganisations() {
        List<Activity> allActivities = activitiesRepository.findAll();

        return new ResponseEntity<>(getListOfActivitiesDTOForResponse(allActivities), HttpStatus.OK);
    }

    /**
     * Activities by organisation username from token
     */
    public ResponseEntity<?> getMyActivities(Principal principal) {
        String username = principal.getName();
        Optional<AppUser> appUserByUsername = userService.findAppUserByUsername(username);
        if (appUserByUsername.isEmpty()) {
            return new ResponseEntity<>("Bad token, try again!", HttpStatus.BAD_REQUEST);
        }
        OrganisationInfo additionalInfoAboutOrganisation = organisationService.findAdditionalInfoAboutOrganisation(appUserByUsername.get());
        List<Activity> activitiesByAppUser = activitiesRepository.findActivitiesByAppUser(appUserByUsername.get());

        ActivitiesDTO activitiesDTO = activitiesFromEntityToDto(additionalInfoAboutOrganisation, activitiesByAppUser);

        return new ResponseEntity<>(activitiesDTO, HttpStatus.OK);
    }

    /**
     * All activities of current organisation by organisation name
     */
    public ResponseEntity<?> getAllActivitiesFromCurrentOrganisation(String nameOfOrganisation) {
        Optional<OrganisationInfo> organisationByNameOfOrganisation = organisationService.findOrganisationByNameOfOrganisation(nameOfOrganisation);
        if (organisationByNameOfOrganisation.isEmpty()) {
            return new ResponseEntity<>("Organisation with name " + nameOfOrganisation + " not found", HttpStatus.BAD_REQUEST);
        }
        OrganisationInfo organisationInfo = organisationByNameOfOrganisation.get();

        List<Activity> activitiesByAppUser = activitiesRepository.findActivitiesByAppUser(organisationInfo.getAppUser());
        ActivitiesDTO activitiesDTO = activitiesFromEntityToDto(organisationInfo, activitiesByAppUser);

        return new ResponseEntity<>(activitiesDTO, HttpStatus.OK);
    }

    public List<ActivitiesDTO> getOrganisationsWithActivitiesByPreferences(List<String> preferences) {
        List<Activity> activityByKindOfActivity = activitiesRepository.findActivityByKindOfActivityIgnoreCaseIn(preferences);
        return getListOfActivitiesDTOForResponse(activityByKindOfActivity);
    }


    /**
     * Delete activity by id and from org principal (organisation data)
     */
    @SneakyThrows
    public ResponseEntity<?> deleteActivityById(Long id, Principal principal) {
        AppUser appUser = organisationService.findOrganisationByUsername(principal.getName()).get();
        Optional<Activity> activityById = activitiesRepository.findById(id);

        if (activityById.isEmpty() || !appUser.equals(activityById.get().getAppUser())) {
            return new ResponseEntity<>("Bad id", HttpStatus.BAD_REQUEST);
        }

        activitiesRepository.deleteById(id);

        return new ResponseEntity<>("Successfully deleted activity!", HttpStatus.FOUND);
    }

    public ResponseEntity<?> addVolunteerToActivity(Principal principal, Long idOfActivity) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        List<VolunteerInActivity> allByUser = volunteersInActivityRepository.findAllByUser(appUser);

        if (allByUser.size() > 5) {
            return new ResponseEntity<>("To much activities in yours profile!", HttpStatus.OK);
        }

        Optional<Activity> activityById = activitiesRepository.findById(idOfActivity);

        if (activityById.isEmpty()) {
            return new ResponseEntity<>("No such activity in our database", HttpStatus.BAD_REQUEST);
        }

        VolunteerInActivity volunteerInActivity = new VolunteerInActivity();
        volunteerInActivity.setUser(appUser);
        volunteerInActivity.setActivity(activityById.get());
        volunteersInActivityRepository.save(volunteerInActivity);
        return new ResponseEntity<>("Successful! Welcome to activity: " + activityById.get().getTitle(), HttpStatus.OK);
    }

    public ResponseEntity<?> updateActivityInformation(Long idOfActivity, AddActivityRequestDTO activityRequestDTO, Principal principal) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        Optional<Activity> activityById = activitiesRepository.findById(idOfActivity);
        if (activityById.isEmpty() || !appUser.equals(activityById.get().getAppUser())) {
            return new ResponseEntity<>("Bad id of activity!", HttpStatus.BAD_REQUEST);
        }

        Activity activity = activityById.get();

        activity.setTitle(activityRequestDTO.getTitle());
        activity.setTitle(activityRequestDTO.getTitle());
        activity.setDescription(activityRequestDTO.getDescription());
        activity.setCountry(activityRequestDTO.getCountry());
        activity.setCity(activityRequestDTO.getCity());
        activity.setDateOfPlace(new Date());
        activity.setKindOfActivity(activityRequestDTO.getKindOfActivity());

        sendNotificationForSubscribers(activity, "Updated");

        return new ResponseEntity<>("Successfully updated id", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteVolunteerFromActivity(Long id, Principal principal) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName()).get();
        if (!volunteersInActivityRepository.existsByUserAndActivity_Id(appUser, id)) {
            return new ResponseEntity<>("Bad id of activity!", HttpStatus.BAD_REQUEST);
        }
        volunteersInActivityRepository.deleteByActivity_IdAndUser_Id(id, appUser.getId());
        return new ResponseEntity<>("Successfully leaved from activity!", HttpStatus.OK);
    }

    public List<ActivitiesDTO> getActivitiesOfVolunteer(AppUser appUser) {
        List<VolunteerInActivity> allByUser = volunteersInActivityRepository.findAllByUser(appUser);
        List<Activity> infoAboutActivities = allByUser.stream().map(volunteerInActivity ->
                activitiesRepository.findById(volunteerInActivity.getActivity().getId()).get()).toList();
        return getListOfActivitiesDTOForResponse(infoAboutActivities);
    }

    public ResponseEntity<?> findNearbyActivities(NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        List<ActivitiesDTO> activitiesByPlace = getListOfActivitiesDTOForResponse(
                activitiesRepository.findActivityByCountryAndCity(nearbyActivitiesRequestDTO.getCountry(), nearbyActivitiesRequestDTO.getCity()));
        if (activitiesByPlace.isEmpty()) {
            return new ResponseEntity<>("No such activities in current place", HttpStatus.OK);
        }
        return new ResponseEntity<>(activitiesByPlace, HttpStatus.OK);
    }

    public ActivityDTO getActivityDTOFromIdOfActivity(Long idOfActivity){
        Optional<Activity> activityFromDb = activitiesRepository.findById(idOfActivity);
        if (activityFromDb.isEmpty()){
            return null;
        }
        Activity activityById = activityFromDb.get();
        ActivityDTO activity = new ActivityDTO();

        activity.setTitle(activityById.getTitle());
        activity.setDescription(activityById.getDescription());
        activity.setCountry(activityById.getCountry());
        activity.setCity(activityById.getCity());
        activity.setDateOfPlace(new Date());
        activity.setKindOfActivity(activityById.getKindOfActivity());
        return activity;
    }

    public Optional<Activity> findActivityByOrganisationAndIdOfActivity(AppUser appUser, Long idOfActivity) {
        return activitiesRepository.findActivityByAppUserAndId(appUser, idOfActivity);
    }

    /**
     * Methods to convert entities from DB to DTO for response
     */
    private static ActivitiesDTO activitiesFromEntityToDto(OrganisationInfo additionalInfoAboutOrganisation, List<Activity> activitiesByAppUser) {
        ActivitiesDTO activitiesDTO = new ActivitiesDTO();

        OrganisationResponseDTO responseDTO = getOrganisationResponseDTO(additionalInfoAboutOrganisation);

        for (Activity activity : activitiesByAppUser) {
            activitiesDTO.addActivity(new ActivityDTO(activity.getId(),
                    activity.getCity(),
                    activity.getCountry(),
                    activity.getDateOfPlace(),
                    activity.getDescription(),
                    activity.getTitle(),
                    activity.getKindOfActivity()));
        }

        activitiesDTO.setOrganisationResponseDTO(responseDTO);
        return activitiesDTO;
    }

    private static OrganisationResponseDTO getOrganisationResponseDTO(OrganisationInfo additionalInfoAboutOrganisation) {
        return new OrganisationResponseDTO(
                additionalInfoAboutOrganisation.getAppUser().getId(),
                additionalInfoAboutOrganisation.getNameOfOrganisation(),
                additionalInfoAboutOrganisation.getCountry(),
                additionalInfoAboutOrganisation.getCity(),
                additionalInfoAboutOrganisation.getAddress());
    }


    private List<ActivitiesDTO> getListOfActivitiesDTOForResponse(List<Activity> activities) {
        List<ActivitiesDTO> responseActivities = new ArrayList<>();

        Map<AppUser, List<Activity>> activitiesByOrganisationMap = activities.stream()
                .collect(Collectors.groupingBy(Activity::getAppUser));

        for (Map.Entry<AppUser, List<Activity>> organisationWithActivity : activitiesByOrganisationMap.entrySet()) {
            responseActivities.add(activitiesFromEntityToDto(organisationService.findAdditionalInfoAboutOrganisation(organisationWithActivity.getKey()),
                    organisationWithActivity.getValue()));
        }
        return responseActivities;
    }
}
