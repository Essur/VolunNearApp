package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.RegistrationOrganizationRequest;
import com.volunnear.dtos.requests.UpdateOrganizationInfoRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrganizationController {
    private final ActivityService activityService;
    private final OrganizationService organizationService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = Routes.REGISTER_ORGANIZATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer registrationOfOrganization(@RequestBody RegistrationOrganizationRequest registrationOrganizationRequest) {
        return organizationService.registerOrganization(registrationOrganizationRequest);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = Routes.UPDATE_ORGANIZATION_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrganizationResponseDTO updateOrganizationInfo(@RequestBody UpdateOrganizationInfoRequest updateOrganizationInfoRequest, Principal principal) {
        return organizationService.updateOrganizationInfo(updateOrganizationInfoRequest, principal);
    }

    @Operation(summary = "Get all organizations", description = "Returns List<OrganizationResponseDTO>")
    @ResponseStatus(value = HttpStatus.OK)
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
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_ORGANIZATION_PROFILE)
    public ActivitiesDTO getOrganizationProfile(Principal principal) {
        Organization organizationProfile = organizationService.getOrganizationProfile(principal);
        return activityService.getMyActivities(organizationProfile);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_ORGANIZATION_ID)
    public Integer getOrganizationId(Principal principal) {
        return organizationService.getOrganizationId(principal);
    }

    @Operation(summary = "Delete organization profile", description = "Successfully or not deleted user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data was successfully deleted",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad credentials, try re-login")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.DELETE_ORGANIZATION_PROFILE)
    public void deleteOrganizationProfile(Principal principal) {
        organizationService.deleteOrganizationProfile(principal);
    }
}
