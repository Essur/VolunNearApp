package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.response.VolunteerActivityRequestStatus;
import com.volunnear.entitiy.activities.VolunteerActivityRequest;
import com.volunnear.services.activities.VolunteerActivityRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public void kickVolunteerFromActivity(@RequestParam Integer requestId, Principal principal) {
        volunteerActivityRequestService.kickVolunteerFromActivity(requestId, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_VOLUNTEER_ACTIVITY_REQUEST_STATUS_INFO)
    public VolunteerActivityRequestStatus getActivityInfoWithVolunteerRequestStatusInfo(@RequestParam Integer activityId, Principal principal) {
        VolunteerActivityRequest requestStatusInfo = volunteerActivityRequestService.getRequestStatusInfo(activityId, principal);
        return new VolunteerActivityRequestStatus(requestStatusInfo.getStatus().toString());
    }
//    TODO: @GetMapping(value = Routes.GET_ORGANIZATION_REQUESTS)
//    TODO: @GetMapping(value = Routes.GET_VOLUNTEER_REQUESTS)
}
