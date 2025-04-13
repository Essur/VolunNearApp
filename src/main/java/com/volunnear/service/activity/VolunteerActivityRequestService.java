package com.volunnear.service.activity;

import com.volunnear.ActivityRequestStatus;
import com.volunnear.dto.VolunteerInfo;
import com.volunnear.dto.response.ActivityRequestInfoResponse;
import com.volunnear.dto.response.OrganizationActivityRequestInfoResponse;
import com.volunnear.entity.activities.Activities;
import com.volunnear.entity.activities.Activity;
import com.volunnear.entity.activities.VolunteerActivityRequest;
import com.volunnear.entity.infos.Volunteer;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.repository.activities.VolunteerActivityRequestRepository;
import com.volunnear.service.user.OrganizationService;
import com.volunnear.service.user.VolunteerService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerActivityRequestService {
    private final EntityManager entityManager;
    private final ActivityService activityService;
    private final VolunteerService volunteerService;
    private final OrganizationService organizationService;
    private final VolunteerActivityRequestRepository requestRepository;

    @Transactional
    public Integer createRequest(Integer activityId, Principal principal) {
        VolunteerActivityRequest request = new VolunteerActivityRequest();
        Optional<Volunteer> volunteerInfo = volunteerService.getVolunteerInfo(principal);
        if (volunteerInfo.isEmpty()) {
            throw new BadUserCredentialsException("User not found, try re-login");
        }
        Activity activity = entityManager.find(Activity.class, activityId);

        request.setVolunteer(volunteerInfo.get());
        request.setActivity(activity);
        requestRepository.save(request);
        return request.getId();
    }

    @Transactional
    public Integer approveRequest(Integer requestId, Principal principal) {
        VolunteerActivityRequest request = requestRepository.findById(requestId).orElseThrow();
        boolean result = activityService.addVolunteerToActivity(request.getVolunteer(), request.getActivity().getId(), principal);
        if (!result) {
            throw new BadUserCredentialsException("User not found, try re-login");
        }
        request.setStatus(ActivityRequestStatus.APPROVED);
        request.setApprovedDate(Instant.now());
        requestRepository.save(request);
        return request.getId();
    }

    @Transactional
    public void deleteMyJoinActivityRequest(Integer activityId, Principal principal) {
        Optional<Volunteer> volunteerInfo = volunteerService.getVolunteerInfo(principal);
        if (volunteerInfo.isEmpty()) {
            throw new BadUserCredentialsException("User not found, try re-login");
        }
        requestRepository.deleteByActivityIdAndVolunteer(activityId, volunteerInfo.get());
    }

    @Transactional
    public void kickVolunteerFromActivity(String volunteerUsername, Integer activityId, Principal organizationPrincipal) {
        Optional<VolunteerActivityRequest> request = requestRepository.findByVolunteer_User_UsernameAndActivityId(volunteerUsername,activityId);
        if (request.isEmpty()) {
            throw new DataNotFoundException("Request for user with username " + volunteerUsername + " not found");
        }
        if (!organizationService.isUserAreOrganization(organizationPrincipal.getName()) ||
            !activityService.isActivityBelongToOrganization(request.get().getActivity(), organizationPrincipal)) {
            throw new BadUserCredentialsException("Bad user credentials, you are not organization");
        }
        activityService.deleteVolunteerFromActivity(request.get().getActivity(), request.get().getVolunteer());
        requestRepository.delete(request.get());
    }

    @Transactional
    public void leaveActivity(Integer activityId, Principal principal) {
        Optional<Volunteer> volunteerInfo = volunteerService.getVolunteerInfo(principal);
        if (volunteerInfo.isEmpty()) {
            throw new BadUserCredentialsException("User not found, try re-login");
        }
        Activity activity = entityManager.find(Activity.class, activityId);
        activityService.deleteVolunteerFromActivity(activity, volunteerInfo.get());
        requestRepository.deleteByActivityIdAndVolunteer(activityId, volunteerInfo.get());
    }

    public List<OrganizationActivityRequestInfoResponse> getVolunteersActivityRequestsForOrganization(Principal principal) {
        List<Activities> activities = activityService.getActivitiesOfOrganizationByUsername(principal);
        if (activities.isEmpty()) {
            throw new DataNotFoundException("There is no activity in your profile");
        }
        List<VolunteerActivityRequest> allRequests = requestRepository.findAllByActivityInAndStatusLike(
                activities.stream().map(Activities::getActivity).toList(), ActivityRequestStatus.PENDING);

        if (allRequests.isEmpty()) {
            throw new DataNotFoundException("There is no activity requests in your profile");
        }
        return allRequests.stream().map(r -> new OrganizationActivityRequestInfoResponse(
                r.getId(),
                r.getActivity().getId(),
                r.getActivity().getTitle(),
                new VolunteerInfo(r.getVolunteer().getEmail(),
                        r.getVolunteer().getUser().getUsername(),
                        r.getVolunteer().getFirstName(),
                        r.getVolunteer().getLastName()))).toList();
    }

    public List<ActivityRequestInfoResponse> getVolunteerActivityRequestsForVolunteer(Principal principal) {
        Optional<Volunteer> volunteerInfo = volunteerService.getVolunteerInfo(principal);
        if (volunteerInfo.isEmpty()) {
            throw new BadUserCredentialsException("User not found, try re-login");
        }
        List<VolunteerActivityRequest> allVolunteerRequests = requestRepository.findAllByVolunteerAndStatusLike(volunteerInfo.get(), ActivityRequestStatus.PENDING);
        if (allVolunteerRequests.isEmpty()) {
            throw new DataNotFoundException("There is no activity requests in your profile");
        }

        return allVolunteerRequests.stream().map(r -> new ActivityRequestInfoResponse(r.getId(),
                r.getActivity().getId(),
                r.getActivity().getTitle())).toList();
    }

    public VolunteerActivityRequest getRequestStatusInfo(Integer activityId, Principal principal) {
        Optional<VolunteerActivityRequest> activityRequest = requestRepository.findByActivityIdAndVolunteer_User_Username(activityId, principal.getName());
        return activityRequest.orElseThrow(() -> new DataNotFoundException("Is no request for that activity"));
    }
}
