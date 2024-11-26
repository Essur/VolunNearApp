package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.services.activities.ActivityService;
import com.volunnear.services.users.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrganizationController {
    private final ActivityService activityService;
    private final OrganizationService organizationService;

    @Operation(summary = "Get all organizations", description = "Returns List<OrganizationResponseDTO>")
    @GetMapping(value = Routes.GET_ALL_ORGANIZATIONS)
    public List<OrganizationResponseDTO> getAllOrganizations() {
        return organizationService.getAllOrganizationsWithInfo();
    }

    @Operation(summary = "Get all organizations", description = "Returns ActivitiesDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities dto",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad token, try again!")
    })
    @GetMapping(value = Routes.GET_ORGANIZATION_PROFILE)
    public ActivitiesDTO getOrganizationProfile(Principal principal) {
        Organization organizationProfile = organizationService.getOrganizationProfile(principal);
        return activityService.getMyActivities(organizationProfile);
    }

    @Operation(summary = "Delete organization profile", description = "Successfully or not deleted user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data was successfully deleted",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad credentials, try re-login")
    })
    @DeleteMapping(value =  Routes.DELETE_ORGANIZATION_PROFILE)
    public ResponseEntity<String> deleteOrganizationProfile(Principal principal) {
        return organizationService.deleteOrganizationProfile(principal);
    }
}
