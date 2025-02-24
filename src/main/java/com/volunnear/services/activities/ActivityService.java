package com.volunnear.services.activities;

import com.volunnear.dtos.ActivityNotificationDTO;
import com.volunnear.dtos.requests.AddActivityRequest;
import com.volunnear.dtos.requests.NearbyActivitiesRequest;
import com.volunnear.dtos.requests.UpdateActivityInfoRequest;
import com.volunnear.dtos.response.*;
import com.volunnear.entitiy.activities.*;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.events.ActivityCreationEvent;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.repositories.activities.ActivitiesRepository;
import com.volunnear.repositories.activities.ActivityRepository;
import com.volunnear.repositories.activities.VolunteersInActivityRepository;
import com.volunnear.services.users.OrganizationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ActivityRepository activityRepository;
    private final OrganizationService organizationService;
    private final ApplicationEventPublisher eventPublisher;
    private final ActivitiesRepository activitiesRepository;
    private final VolunteersInActivityRepository volunteersInActivityRepository;

    @Transactional
    public Integer addActivityToOrganization(AddActivityRequest activityRequest, Principal principal) {
        Optional<Organization> organization = organizationService.findOrganizationByUsername(principal.getName());
        if (organization.isEmpty()) {
            throw new BadUserCredentialsException("User with username " + principal.getName() + " was not found");
        }

        Activity activity = Activity.builder()
                .title(activityRequest.getTitle())
                .description(activityRequest.getDescription())
                .country(activityRequest.getCountry())
                .city(activityRequest.getCity())
                .kindOfActivity(activityRequest.getKindOfActivity())
                .dateOfPlace(Instant.now()).build();
        activityRepository.save(activity);

        Organization org = organization.get();

        Activities activities = new Activities();
        activities.setId(new ActivitiesId(org, activity));
        activities.setActivity(activity);
        activities.setOrganization(org);
        activitiesRepository.save(activities);
        sendNotificationForSubscribers(activity, "New");

        return activity.getId();
    }

    @Async
    public void sendNotificationForSubscribers(Activity activity, String status) {
        ActivityNotificationDTO notificationDTO = new ActivityNotificationDTO();
        ActivityDTO activityDTO = new ActivityDTO();

        activityDTO.setId(activity.getId());
        activityDTO.setCity(activity.getCity());
        activityDTO.setCountry(activity.getCountry());
        activityDTO.setKindOfActivity(activity.getKindOfActivity());
        activityDTO.setTitle(activity.getTitle());
        activityDTO.setDescription(activity.getDescription());
        activityDTO.setDateOfPlace(activity.getDateOfPlace());

        OrganizationResponseDTO organizationResponseDTO = getOrganizationResponseDTO(
                activitiesRepository.findActivitiesByActivityId(activity.getId()).get().getOrganization());
        notificationDTO.setActivityDTO(activityDTO);
        notificationDTO.setOrganizationResponseDTO(organizationResponseDTO);
        eventPublisher.publishEvent(new ActivityCreationEvent(this, notificationDTO, status));
    }

    public List<ActivityDTO> getAllActivities() {
        List<Activities> allActivities = activitiesRepository.findAll();
        if (allActivities.isEmpty()) {
            throw new DataNotFoundException("Activities list is empty");
        }
        return getListOfAllActivities(allActivities);
    }

    /**
     * Activities by organization username from token
     */
    public List<ActivityDTO> getMyActivities(Principal principal) {
        List<Activities> allByOrganizationUsername = activitiesRepository.findAllByOrganization_Username(principal.getName());
        return getListOfAllActivities(allByOrganizationUsername);
    }

    /**
     * All activities of current organization by organization name
     */
    public ActivitiesDTO getAllActivitiesFromCurrentOrganization(Integer id) {
        List<Activities> allByOrganizationName = activitiesRepository.findAllByOrganization_Id(id);
        Optional<Organization> organizationByNameOfOrganization = organizationService.findOrganizationById(id);
        if (organizationByNameOfOrganization.isEmpty()) {
            throw new DataNotFoundException("Organization with id " + id + " not found");
        }

        return activitiesFromEntityToDto(allByOrganizationName, organizationByNameOfOrganization.get());
    }

    public List<Activities> getActivitiesOfOrganizationByUsername(Principal principal) {
        return activitiesRepository.findActivitiesByOrganization_Username(principal.getName());
    }

    /**
     * Delete activity by id and from org principal (organization data)
     */
    @SneakyThrows
    @Transactional
    public void deleteActivityById(Integer id, Principal principal) {
        Optional<Activities> activityById = activitiesRepository.findActivitiesByActivityId(id);

        if (activityById.isEmpty() || !principal.getName().equals(activityById.get().getOrganization().getUsername())) {
            throw new DataNotFoundException("Activity with id " + id + " not found");
        }
        activitiesRepository.delete(activityById.get());
        activityRepository.deleteById(id);
    }

    @Transactional
    public boolean addVolunteerToActivity(Volunteer volunteer, Integer idOfActivity, Principal organizationPrincipal) {
        Optional<Activity> activityById = activityRepository.findById(idOfActivity);

        if (activityById.isEmpty() || activitiesRepository.findByOrganization_UsernameAndActivity_Id(organizationPrincipal.getName(), idOfActivity).isEmpty()) {
            throw new DataNotFoundException("No such activity in our database");
        }

        VolunteersInActivity volunteerInActivity = new VolunteersInActivity();

        volunteerInActivity.setId(new VolunteersInActivityId(activityById.get().getId(), volunteer.getId()));
        volunteerInActivity.setActivity(activityById.get());
        volunteerInActivity.setVolunteer(volunteer);
        volunteerInActivity.setDateOfEntry(Instant.now());
        volunteersInActivityRepository.save(volunteerInActivity);
        return true;
    }

    public ActivityDTO updateActivityInformation(Integer idOfActivity, UpdateActivityInfoRequest activityRequest, Principal principal) {
        Optional<Activities> activityById = activitiesRepository.findActivitiesByActivityId(idOfActivity);
        if (activityById.isEmpty() || !principal.getName().equals(activityById.get().getOrganization().getUsername())) {
            throw new DataNotFoundException("Activity with id " + idOfActivity + " was not found");
        }

        Activity activity = activityById.get().getActivity();

        activity.setTitle(activityRequest.getTitle());
        activity.setDescription(activityRequest.getDescription());
        activity.setCountry(activityRequest.getCountry());
        activity.setCity(activityRequest.getCity());
        activity.setDateOfPlace(Instant.now());
        activity.setKindOfActivity(activityRequest.getKindOfActivity());
        activityRepository.save(activity);
        sendNotificationForSubscribers(activity, "Updated");

        return activityToDTO(activity);
    }

    @Transactional
    public void deleteVolunteerFromActivity(Activity activity, Volunteer volunteer) {
        volunteersInActivityRepository.deleteByActivityAndVolunteer(activity, volunteer);
    }

    public boolean isActivityBelongToOrganization(Activity activity, Principal principal) {
        OrganizationResponseDTO organizationProfile = organizationService.getOrganizationProfile(principal);
        return activitiesRepository.existsByActivityAndOrganizationId(activity, organizationProfile.getId());
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

    public List<ActivitiesDTO> findNearbyActivities(NearbyActivitiesRequest nearbyActivitiesRequest) {
        List<ActivitiesDTO> activitiesByPlace = getListOfActivitiesDTOForResponse(
                activitiesRepository.findAllByActivity_CountryAndActivity_City(nearbyActivitiesRequest.getCountry(), nearbyActivitiesRequest.getCity()));
        if (activitiesByPlace.isEmpty()) {
            throw new DataNotFoundException("No such activities in "
                                            + nearbyActivitiesRequest.getCountry() + " "
                                            + nearbyActivitiesRequest.getCity());
        }
        return activitiesByPlace;
    }

    public Optional<Activities> findActivityByOrganizationAndIdOfActivity(Principal principal, Integer idOfActivity) {
        return activitiesRepository.findByOrganization_UsernameAndActivity_Id(principal.getName(), idOfActivity);
    }

    public ActivityInfoDTO getActivityInfoByActivityId(Integer activityId) {
        Optional<Activities> activitiesByActivityId = activitiesRepository.findActivitiesByActivity_Id(activityId);
        if (activitiesByActivityId.isEmpty()) {
            throw new DataNotFoundException("Activity with id " + activityId + " was not found");
        }
        return getActivityInfoDTO(activitiesByActivityId.get());
    }

    public List<VolunteerInActivityInfo> getAllVolunteersInCurrentActivity(Integer activityId, Principal principal) {
        if (!activitiesRepository.existsByOrganization_UsernameAndActivity_Id(principal.getName(), activityId)) {
            throw new BadUserCredentialsException("Invalid credentials, try re-login");
        }
        List<VolunteersInActivity> allByActivityId = volunteersInActivityRepository.findAllByActivity_Id(activityId);
        if (allByActivityId.isEmpty()) {
            throw new DataNotFoundException("List of volunteers in that activity are empty");
        }
        
        return allByActivityId.stream().map(volunteersInActivity -> new VolunteerInActivityInfo(
                volunteersInActivity.getVolunteer().getEmail(),
                volunteersInActivity.getVolunteer().getUsername(),
                volunteersInActivity.getVolunteer().getFirstName(),
                volunteersInActivity.getVolunteer().getLastName(),
                volunteersInActivity.getDateOfEntry()
        )).toList();
    }

    /**
     * Methods to convert entities from DB to DTO for response
     */

    private ActivityInfoDTO getActivityInfoDTO(Activities activityEntity) {
        ActivityInfoDTO activityInfoDTO = new ActivityInfoDTO();
        Activity activity = activityEntity.getActivity();
        activityInfoDTO.setOrganizationId(activityEntity.getOrganization().getId());

        activityInfoDTO.setId(activity.getId());
        activityInfoDTO.setTitle(activity.getTitle());
        activityInfoDTO.setDescription(activity.getDescription());
        activityInfoDTO.setCountry(activity.getCountry());
        activityInfoDTO.setCity(activity.getCity());
        activityInfoDTO.setDateOfPlace(activity.getDateOfPlace());
        activityInfoDTO.setKindOfActivity(activity.getKindOfActivity());

        return activityInfoDTO;
    }

    private ActivitiesDTO activitiesFromEntityToDto(List<Activities> activitiesByAppUser, Organization organization) {
        ActivitiesDTO activitiesDTO = new ActivitiesDTO();
        activitiesDTO.setOrganizationResponseDTO(getOrganizationResponseDTO(organization));
        if (!activitiesByAppUser.isEmpty()) {
            for (Activities activities : activitiesByAppUser) {
                activitiesDTO.addActivity(new ActivityDTO(activities.getActivity().getId(),
                        activities.getActivity().getCity(),
                        activities.getActivity().getCountry(),
                        activities.getActivity().getDateOfPlace(),
                        activities.getActivity().getDescription(),
                        activities.getActivity().getTitle(),
                        activities.getActivity().getKindOfActivity()));
            }
        }

        return activitiesDTO;
    }

    private OrganizationResponseDTO getOrganizationResponseDTO(Organization organization) {
        return new OrganizationResponseDTO(
                organization.getId(),
                organization.getName(),
                organization.getCountry(),
                organization.getCity(),
                organization.getAddress(),
                organization.getEmail()
        );
    }

    private List<ActivityDTO> getListOfAllActivities(List<Activities> activities) {
        return new ArrayList<>(activities.stream().map(Activities::getActivity).map(activity ->
                new ActivityDTO(
                        activity.getId(),
                        activity.getCity(),
                        activity.getCountry(),
                        activity.getDateOfPlace(),
                        activity.getDescription(),
                        activity.getTitle(),
                        activity.getKindOfActivity()
                )).toList());
    }

    private List<ActivitiesDTO> getListOfActivitiesDTOForResponse(List<Activities> activities) {
        List<ActivitiesDTO> responseActivities = new ArrayList<>();

        Map<Organization, List<Activity>> activitiesByOrganizationMap = activities.stream()
                .collect(Collectors.groupingBy(Activities::getOrganization,
                        Collectors.mapping(Activities::getActivity, Collectors.toList())));

        for (Map.Entry<Organization, List<Activity>> organizationWithActivity : activitiesByOrganizationMap.entrySet()) {
            Organization organization = organizationWithActivity.getKey();
            List<Activity> activitiesList = organizationWithActivity.getValue();
            List<ActivityDTO> activityDtoList = activitiesList.stream()
                    .map(this::activityToDTO)
                    .collect(Collectors.toList());

            ActivitiesDTO activitiesDto = new ActivitiesDTO(activityDtoList, getOrganizationResponseDTO(organization));
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
                activity.getKindOfActivity()
        );
    }


}
