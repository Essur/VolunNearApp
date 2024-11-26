package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.DeletePreferenceFromVolunteerProfileRequestDTO;
import com.volunnear.dtos.requests.PreferencesRequestDTO;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.services.activities.RecommendationService;
import com.volunnear.services.users.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;
    private final RecommendationService recommendationService;

    @GetMapping(value = Routes.GET_VOLUNTEER_PROFILE)
    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        return volunteerService.getVolunteerProfile(principal);
    }

    @Operation(summary = "Set preferences on user account", description = "Requires volunteer account (token) and preferencesRequestDTO")
    @PostMapping(value = Routes.SET_VOLUNTEERS_PREFERENCES)
    public String setVolunteersPreferences(@RequestBody PreferencesRequestDTO preferencesRequestDTO, Principal principal) {
        return volunteerService.setPreferencesForVolunteer(preferencesRequestDTO, principal);
    }

    @Operation(summary = "Get recommendations by preferences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Preferences founded",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ActivitiesDTO.class)))),
            @ApiResponse(responseCode = "400", description = "In your profile no preferences set or Activities by your preferences not founded")
    })
    @GetMapping(value = Routes.GET_RECOMMENDATION_BY_PREFERENCES)
    public ResponseEntity<?> getRecommendationsByPreferencesOfUser(Principal principal) {
        return recommendationService.generateRecommendations(principal);
    }

    @Operation(summary = "Remove preference from profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Preference removed"),
            @ApiResponse(responseCode = "400", description = "Preference not found"),
            @ApiResponse(responseCode = "404", description = "Error, preference was not removed")
    })
    @DeleteMapping(value = Routes.DELETE_PREFERENCE_BY_ID)
    public ResponseEntity<?> deletePreferenceById(@RequestBody DeletePreferenceFromVolunteerProfileRequestDTO preferenceId, Principal principal) {
        return volunteerService.deletePreferenceById(preferenceId, principal);
    }

    @Operation(summary = "Delete volunteer profile", description = "Successfully or not deleted user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data was successfully deleted",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad credentials, try re-login")
    })
    @DeleteMapping(value =  Routes.DELETE_VOLUNTEER_PROFILE)
    public ResponseEntity<String> deleteVolunteerProfile(Principal principal) {
        return volunteerService.deleteVolunteerProfile(principal);
    }
}