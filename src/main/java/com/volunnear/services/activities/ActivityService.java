package com.volunnear.services.activities;

import com.volunnear.dtos.ActivityNotificationDTO;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.activities.*;
import com.volunnear.entitiy.infos.Organisation;
import com.volunnear.entitiy.infos.Preference;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.events.ActivityCreationEvent;
import com.volunnear.repositories.activities.ActivitiesRepository;
import com.volunnear.repositories.activities.ActivityRepository;
import com.volunnear.repositories.activities.VolunteersInActivityRepository;
import com.volunnear.repositories.infos.PreferenceRepository;
import com.volunnear.services.users.OrganisationService;
import com.volunnear.services.users.VolunteerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private VolunteerService volunteerService;
    private final ActivityRepository activityRepository;
    private final OrganisationService organisationService;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivitiesRepository activitiesRepository;
    private final PreferenceRepository preferenceRepository;
    private final VolunteersInActivityRepository volunteersInActivityRepository;

    @Lazy
    @Autowired
    public void setVolunteerService(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @Transactional
    public ResponseEntity<?> addActivityToOrganisation(AddActivityRequestDTO activityRequest, Principal principal) {
        Optional<Organisation> organisation = organisationService.findOrganisationByUsername(principal.getName());
        if (organisation.isEmpty()) {
            return new ResponseEntity<>("Bad username!", HttpStatus.OK);
        }
        if (!preferenceRepository.existsPreferenceByNameIgnoreCase(activityRequest.getKindOfActivity())){
            Preference newPreference = new Preference();
            newPreference.setName(activityRequest.getKindOfActivity());
            preferenceRepository.save(newPreference);
        }
        Preference preference = preferenceRepository.findPreferenceByNameIgnoreCase(activityRequest.getKindOfActivity()).get();
        Activity activity = Activity.builder()
                .title(activityRequest.getTitle())
                .description(activityRequest.getDescription())
                .country(activityRequest.getCountry())
                .city(activityRequest.getCity())
                .kindOfActivity(preference)
                .dateOfPlace(Instant.now()).build();
        activityRepository.save(activity);

        Organisation org = organisation.get();

        Activities activities = new Activities();
        activities.setId(new ActivitiesId(org.getId(), activity.getId()));
        activities.setActivity(activity);
        activities.setOrganisation(org);
        activitiesRepository.save(activities);
        sendNotificationForSubscribers(activity, "New");

        return new ResponseEntity<>("Activity added",HttpStatus.OK);
    }

    @Async
    public void sendNotificationForSubscribers(Activity activity, String status) {
        ActivityNotificationDTO notificationDTO = new ActivityNotificationDTO();
        ActivityDTO activityDTO = new ActivityDTO();

        activityDTO.setId(activity.getId());
        activityDTO.setCity(activity.getCity());
        activityDTO.setCountry(activity.getCountry());
        activityDTO.setKindOfActivity(activity.getKindOfActivity().getName());
        activityDTO.setTitle(activity.getTitle());
        activityDTO.setDescription(activity.getDescription());
        activityDTO.setDateOfPlace(activity.getDateOfPlace());

        OrganisationResponseDTO organisationResponseDTO = getOrganisationResponseDTO(
                activitiesRepository.findActivitiesByActivityId(activity.getId()).get().getOrganisation());
        notificationDTO.setActivityDTO(activityDTO);
        notificationDTO.setOrganisationResponseDTO(organisationResponseDTO);
        eventPublisher.publishEvent(new ActivityCreationEvent(this, notificationDTO, status));
    }

    public List<ActivitiesDTO> getAllActivitiesOfAllOrganisations() {
        List<Activities> allActivities = activitiesRepository.findAll();

        return getListOfActivitiesDTOForResponse(allActivities);
    }

    /**
     * Activities by organisation username from token
     */
    public ActivitiesDTO getMyActivities(Organisation organisation) {
        List<Activities> allByOrganisationUsername = activitiesRepository.findAllByOrganisation_Username(organisation.getUsername());
        return activitiesFromEntityToDto(allByOrganisationUsername, organisation);
    }

    /**
     * All activities of current organisation by organisation name
     */
    public ResponseEntity<?> getAllActivitiesFromCurrentOrganisation(String nameOfOrganisation) {
        List<Activities> allByOrganisationName = activitiesRepository.findAllByOrganisation_Name(nameOfOrganisation);
        Optional<Organisation> organisationByNameOfOrganisation = organisationService.findOrganisationByNameOfOrganisation(nameOfOrganisation);
        if (organisationByNameOfOrganisation.isEmpty()) {
            return new ResponseEntity<>("Organisation with name " + nameOfOrganisation + " not found", HttpStatus.BAD_REQUEST);
        }
        ActivitiesDTO activitiesDTO = activitiesFromEntityToDto(allByOrganisationName, organisationByNameOfOrganisation.get());

        return new ResponseEntity<>(activitiesDTO, HttpStatus.OK);
    }

    public List<ActivitiesDTO> getActivitiesOfOrganisationByPreferences(List<String> preferences) {
        List<Activity> activitiesByKindOfActivity = activityRepository.findAllActivitiesByKindOfActivity_NameIgnoreCaseIn(preferences);
        List<Activities> allByActivityContains = activitiesRepository.findAllByActivityIn(activitiesByKindOfActivity);
        return getListOfActivitiesDTOForResponse(allByActivityContains);
    }


    /**
     * Delete activity by id and from org principal (organisation data)
     */
    @SneakyThrows
    @Transactional
    public ResponseEntity<?> deleteActivityById(Integer id, Principal principal) {
        Optional<Activities> activityById = activitiesRepository.findActivitiesByActivityId(id);

        if (activityById.isEmpty() || !principal.getName().equals(activityById.get().getOrganisation().getUsername())) {
            return new ResponseEntity<>("Bad id", HttpStatus.BAD_REQUEST);
        }
        activitiesRepository.delete(activityById.get());
        activityRepository.deleteById(id);

        return new ResponseEntity<>("Successfully deleted activity!", HttpStatus.FOUND);
    }

    @Transactional
    public ResponseEntity<?> addVolunteerToActivity(Principal principal, Integer idOfActivity) {
        Optional<Volunteer> volunteerInfo = volunteerService.getVolunteerInfo(principal);
        if (volunteerInfo.isEmpty()) {
            return new ResponseEntity<>("Bad credentials, try re-login", HttpStatus.BAD_REQUEST);
        }
        Optional<Activity> activityById = activityRepository.findById(idOfActivity);

        if (activityById.isEmpty()) {
            return new ResponseEntity<>("No such activity in our database", HttpStatus.BAD_REQUEST);
        }

        VolunteersInActivity volunteerInActivity = new VolunteersInActivity();
        volunteerInActivity.setId(new VolunteersInActivityId(activityById.get().getId(),volunteerInfo.get().getId()));
        volunteerInActivity.setActivity(activityById.get());
        volunteerInActivity.setVolunteer(volunteerInfo.get());
        volunteerInActivity.setDateOfEntry(Instant.now());
        volunteersInActivityRepository.save(volunteerInActivity);
        return new ResponseEntity<>("Successful! Welcome to activity: " + activityById.get().getTitle(), HttpStatus.OK);
    }

    public ResponseEntity<?> updateActivityInformation(Integer idOfActivity, AddActivityRequestDTO activityRequest, Principal principal) {
        Optional<Activities> activityById = activitiesRepository.findActivitiesByActivityId(idOfActivity);
        if (activityById.isEmpty() || !principal.getName().equals(activityById.get().getOrganisation().getUsername())) {
            return new ResponseEntity<>("Bad id of activity!", HttpStatus.BAD_REQUEST);
        }

        Activity activity = activityById.get().getActivity();
        Preference preference = preferenceRepository.findPreferenceByNameIgnoreCase(activityRequest.getKindOfActivity())
                .orElseGet(() -> preferenceRepository.save(Preference.builder()
                        .name(activityRequest.getKindOfActivity())
                        .build()));

        activity.setTitle(activityRequest.getTitle());
        activity.setDescription(activityRequest.getDescription());
        activity.setCountry(activityRequest.getCountry());
        activity.setCity(activityRequest.getCity());
        activity.setDateOfPlace(Instant.now());
        activity.setKindOfActivity(preference);
        activityRepository.save(activity);
        sendNotificationForSubscribers(activity, "Updated");

        return new ResponseEntity<>("Successfully updated id " + idOfActivity, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteVolunteerFromActivity(Integer id, Principal principal) {
        if (!volunteersInActivityRepository.existsByActivity_IdAndVolunteer_Username(id, principal.getName())) {
            return new ResponseEntity<>("Bad id of activity!", HttpStatus.BAD_REQUEST);
        }
        volunteersInActivityRepository.deleteByActivity_IdAndVolunteer_Username(id, principal.getName());
        return new ResponseEntity<>("Successfully leaved from activity!", HttpStatus.OK);
    }

    public List<ActivitiesDTO> getActivitiesOfVolunteer(Principal principal) {
        List<VolunteersInActivity> allByUser = volunteersInActivityRepository.findAllVolunteersInActivityByVolunteer_Username(principal.getName());
        Map<Volunteer, List<Activity>> volunteersActivity = allByUser.stream()
                .collect(Collectors.groupingBy(VolunteersInActivity::getVolunteer, Collectors.mapping(VolunteersInActivity::getActivity, Collectors.toList())));
        List<Activities> activitiesWithOrg = new ArrayList<>();
        for (List<Activity> activities : volunteersActivity.values()) {
            activitiesWithOrg = activitiesRepository.findAllByActivityIn(activities);
        }
        return getListOfActivitiesDTOForResponse(activitiesWithOrg);
    }

    public ResponseEntity<?> findNearbyActivities(NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        List<ActivitiesDTO> activitiesByPlace = getListOfActivitiesDTOForResponse(
                activitiesRepository.findAllByActivity_CountryAndActivity_City(nearbyActivitiesRequestDTO.getCountry(), nearbyActivitiesRequestDTO.getCity()));
        if (activitiesByPlace.isEmpty()) {
            return new ResponseEntity<>("No such activities in current place", HttpStatus.OK);
        }
        return new ResponseEntity<>(activitiesByPlace, HttpStatus.OK);
    }

    public Optional<Activities> findActivityByOrganisationAndIdOfActivity(Principal principal, Integer idOfActivity) {
        return activitiesRepository.findByOrganisation_UsernameAndActivity_Id(principal.getName(), idOfActivity);
    }

    /**
     * Methods to convert entities from DB to DTO for response
     */
    private ActivitiesDTO activitiesFromEntityToDto(List<Activities> activitiesByAppUser, Organisation organisation) {
        ActivitiesDTO activitiesDTO = new ActivitiesDTO();
        activitiesDTO.setOrganisationResponseDTO(getOrganisationResponseDTO(organisation));
        if (!activitiesByAppUser.isEmpty()) {
            for (Activities activities : activitiesByAppUser) {
                activitiesDTO.addActivity(new ActivityDTO(activities.getActivity().getId(),
                        activities.getActivity().getCity(),
                        activities.getActivity().getCountry(),
                        activities.getActivity().getDateOfPlace(),
                        activities.getActivity().getDescription(),
                        activities.getActivity().getTitle(),
                        activities.getActivity().getKindOfActivity().getName()));
            }
        }

        return activitiesDTO;
    }

    private OrganisationResponseDTO getOrganisationResponseDTO(Organisation organisation) {
        return new OrganisationResponseDTO(
                organisation.getId(),
                organisation.getName(),
                organisation.getCountry(),
                organisation.getCity(),
                organisation.getAddress(),
                organisation.getEmail()
        );
    }


    private List<ActivitiesDTO> getListOfActivitiesDTOForResponse(List<Activities> activities) {
        List<ActivitiesDTO> responseActivities = new ArrayList<>();

        Map<Organisation, List<Activity>> activitiesByOrganisationMap = activities.stream()
                .collect(Collectors.groupingBy(Activities::getOrganisation,
                        Collectors.mapping(Activities::getActivity, Collectors.toList())));

        for (Map.Entry<Organisation, List<Activity>> organisationWithActivity : activitiesByOrganisationMap.entrySet()) {
            Organisation organisation = organisationWithActivity.getKey();
            List<Activity> activitiesList = organisationWithActivity.getValue();
            List<ActivityDTO> activityDtoList = activitiesList.stream()
                    .map(this::activityToDTO)
                    .collect(Collectors.toList());

            ActivitiesDTO activitiesDto = new ActivitiesDTO(activityDtoList, getOrganisationResponseDTO(organisation));
            responseActivities.add(activitiesDto);
        }

        return responseActivities;
    }

    private ActivityDTO activityToDTO(Activity activity) {
        return new ActivityDTO(
                activity.getId(),
                activity.getCity(),
                activity.getCountry(),
                activity.getDateOfPlace(),
                activity.getDescription(),
                activity.getTitle(),
                activity.getKindOfActivity().getName()
        );
    }
}
