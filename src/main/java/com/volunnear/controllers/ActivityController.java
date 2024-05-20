package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.AddActivityRequestDTO;
import com.volunnear.dtos.requests.NearbyActivitiesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.services.activities.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping(value = Routes.ADD_ACTIVITY, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addActivityToOrganization(@RequestBody AddActivityRequestDTO activityRequest, Principal principal) {
        return activityService.addActivityToOrganization(activityRequest, principal);
    }

    @PostMapping(value = Routes.JOIN_TO_ACTIVITY)
    public ResponseEntity<?> addVolunteerToActivity(@RequestParam Integer id, Principal principal) {
        return activityService.addVolunteerToActivity(principal, id);
    }

    @PutMapping(value = Routes.UPDATE_ACTIVITY_INFORMATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateActivityInformation(@RequestParam Integer idOfActivity,
                                                       @RequestBody AddActivityRequestDTO activityRequestDTO, Principal principal) {
        return activityService.updateActivityInformation(idOfActivity, activityRequestDTO, principal);
    }

    @GetMapping(value = Routes.GET_ALL_ACTIVITIES_WITH_ALL_ORGANIZATIONS)
    public List<ActivitiesDTO> getAllActivitiesOfAllOrganizations() {
        return activityService.getAllActivitiesOfAllOrganizations();
    }

    @Operation(summary = "Get activities of current organization", description = "Returns ActivitiesDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Organization with name not found")
    })
    @GetMapping(value = Routes.ACTIVITY_CURRENT_ORGANIZATION)
    public ResponseEntity<?> getAllActivitiesOfCurrentOrganization(@RequestParam String nameOfOrganization) {
        return activityService.getAllActivitiesFromCurrentOrganization(nameOfOrganization);
    }

    @Operation(summary = "Get activities nearby", description = "Returns List<ActivitiesDTO>")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivitiesDTO.class)))),
            @ApiResponse(responseCode = "400", description = "No such activities in current place")
    })
    @GetMapping(value = Routes.FIND_NEARBY_ACTIVITIES, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findNearbyActivities(@RequestBody NearbyActivitiesRequestDTO nearbyActivitiesRequestDTO) {
        return activityService.findNearbyActivities(nearbyActivitiesRequestDTO);
    }

    @DeleteMapping(value = Routes.DELETE_CURRENT_ACTIVITY_BY_ID)
    public ResponseEntity<?> deleteActivityById(@RequestParam Integer id, Principal principal) {
        return activityService.deleteActivityById(id, principal);
    }

    @DeleteMapping(value = Routes.LEAVE_FROM_ACTIVITY_BY_VOLUNTEER)
    public ResponseEntity<?> deleteVolunteerFromActivity(@RequestParam Integer id, Principal principal) {
        return activityService.deleteVolunteerFromActivity(id, principal);
    }
}
