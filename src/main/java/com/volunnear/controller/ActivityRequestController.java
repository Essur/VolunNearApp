package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.response.ActivityRequestInfoResponse;
import com.volunnear.dto.response.OrganizationActivityRequestInfoResponse;
import com.volunnear.dto.response.VolunteerActivityRequestStatus;
import com.volunnear.entity.activities.VolunteerActivityRequest;
import com.volunnear.service.activity.VolunteerActivityRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityRequestController {
    private final VolunteerActivityRequestService volunteerActivityRequestService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.JOIN_TO_ACTIVITY_REQUEST)
    public Integer joinToActivityRequest(@RequestParam Integer activityId, Principal principal) {
        return volunteerActivityRequestService.createRequest(activityId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.DELETE_MY_JOIN_ACTIVITY_REQUEST)
    public void deleteMyJoinActivityRequest(@RequestParam Integer activityId, Principal principal) {
        volunteerActivityRequestService.deleteMyJoinActivityRequest(activityId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.LEAVE_FROM_ACTIVITY_BY_VOLUNTEER)
    public void leaveActivity(@RequestParam Integer activityId, Principal principal) {
        volunteerActivityRequestService.leaveActivity(activityId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.APPROVE_VOLUNTEER_TO_ACTIVITY)
    public Integer approveVolunteerRequest(@RequestParam Integer requestId, Principal principal) {
        return volunteerActivityRequestService.approveRequest(requestId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.KICK_VOLUNTEER_FORM_ACTIVITY)
    public void kickVolunteerFromActivity(@RequestParam String volunteerUsername,
                                          @RequestParam Integer activityId, Principal principal) {
        volunteerActivityRequestService.kickVolunteerFromActivity(volunteerUsername, activityId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_VOLUNTEER_ACTIVITY_REQUEST_STATUS_INFO)
    public VolunteerActivityRequestStatus getActivityInfoWithVolunteerRequestStatusInfo(@RequestParam Integer activityId, Principal principal) {
        VolunteerActivityRequest requestStatusInfo = volunteerActivityRequestService.getRequestStatusInfo(activityId, principal);
        return new VolunteerActivityRequestStatus(requestStatusInfo.getStatus().toString());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_ORGANIZATION_ACTIVITY_REQUESTS)
    public List<OrganizationActivityRequestInfoResponse> getVolunteersActivityRequestsForOrganization(Principal principal) {
        return volunteerActivityRequestService.getVolunteersActivityRequestsForOrganization(principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_VOLUNTEER_ACTIVITY_REQUESTS)
    public List<ActivityRequestInfoResponse> getVolunteersActivityRequestForVolunteer(Principal principal) {
        return volunteerActivityRequestService.getVolunteerActivityRequestsForVolunteer(principal);
    }
}
