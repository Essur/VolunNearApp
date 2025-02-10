package com.volunnear.services.activities;

import com.volunnear.ActivityRequestStatus;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteerActivityRequest;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.repositories.activities.VolunteerActivityRequestRepository;
import com.volunnear.services.users.OrganizationService;
import com.volunnear.services.users.VolunteerService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
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
    public void kickVolunteerFromActivity(Integer requestId, Principal organizationPrincipal) {
        Optional<VolunteerActivityRequest> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new DataNotFoundException("Request with id " + requestId + " was not found");
        }
        if (!organizationService.isUserAreOrganization(organizationPrincipal.getName()) ||
            activityService.isActivityBelongToOrganization(request.get().getActivity(), organizationPrincipal)) {
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

    public VolunteerActivityRequest getRequestStatusInfo(Integer activityId, Principal principal) {
        Optional<VolunteerActivityRequest> activityRequest = requestRepository.findByActivityIdAndVolunteer_Username(activityId, principal.getName());
        return activityRequest.orElseThrow(() -> new DataNotFoundException("Is no request for that activity"));
    }
}
