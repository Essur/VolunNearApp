package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.AddActivityRequest;
import com.volunnear.dtos.requests.NearbyActivitiesRequest;
import com.volunnear.dtos.requests.UpdateActivityInfoRequest;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.ActivityDTO;
import com.volunnear.dtos.response.ActivityInfoDTO;
import com.volunnear.services.activities.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.ADD_ACTIVITY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer addActivityToOrganization(@RequestBody AddActivityRequest activityRequest, Principal principal) {
        return activityService.addActivityToOrganization(activityRequest, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = Routes.UPDATE_ACTIVITY_INFORMATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ActivityDTO updateActivityInformation(@RequestParam Integer idOfActivity,
                                                 @RequestBody UpdateActivityInfoRequest activityRequestDTO, Principal principal) {
        return activityService.updateActivityInformation(idOfActivity, activityRequestDTO, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_ALL_ACTIVITIES)
    public List<ActivityDTO> getAllActivitiesOfAllOrganizations() {
        return activityService.getAllActivities();
    }

    @Operation(summary = "Get activities of current organization", description = "Returns ActivitiesDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Organization with name not found")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.ACTIVITY_CURRENT_ORGANIZATION)
    public ActivitiesDTO getAllActivitiesOfCurrentOrganization(@RequestParam Integer id) {
        return activityService.getAllActivitiesFromCurrentOrganization(id);
    }

    @Operation(summary = "Get activities nearby", description = "Returns List<ActivitiesDTO>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivitiesDTO.class)))),
            @ApiResponse(responseCode = "400", description = "No such activities in current place")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.FIND_NEARBY_ACTIVITIES, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<ActivitiesDTO> findNearbyActivities(@RequestBody NearbyActivitiesRequest nearbyActivitiesRequest) {
        return activityService.findNearbyActivities(nearbyActivitiesRequest);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_ACTIVITY_INFO)
    public ActivityInfoDTO getActivityInfo(@RequestParam Integer activityId) {
        return activityService.getActivityInfoByActivityId(activityId);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.DELETE_CURRENT_ACTIVITY_BY_ID)
    public void deleteActivityById(@RequestParam Integer id, Principal principal) {
        activityService.deleteActivityById(id, principal);
    }
}
