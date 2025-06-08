package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.request.profile.OrganizationProfileSaveRequestDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.dto.response.profile.OrganizationProfileResponseDTO;
import com.volunnear.service.activity.ActivityService;
import com.volunnear.service.profile.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrganizationController {
    private final ActivityService activityService;
    private final OrganizationService organizationService;

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PostMapping(value = Routes.ORGANIZATION_PROFILE)
    public OrganizationProfileResponseDTO createOrganizationProfile(@RequestBody @Valid OrganizationProfileSaveRequestDTO requestDTO, Principal principal) {
        return organizationService.createOrganizationProfile(requestDTO, principal);
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PutMapping(value = Routes.ORGANIZATION_PROFILE)
    public OrganizationProfileResponseDTO updateOrganizationProfile(@RequestBody @Valid OrganizationProfileSaveRequestDTO requestDTO, Principal principal) {
        return organizationService.updateOrganizationProfile(requestDTO, principal);
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @GetMapping(value = Routes.ORGANIZATION_PROFILE)
    public OrganizationProfileResponseDTO getOrganizationProfile(Principal principal) {
        return organizationService.getOrganizationProfile(principal);
    }

    @GetMapping(value = Routes.ORGANIZATION_BY_ID)
    public OrganizationProfileResponseDTO getOrganizationProfileById(@PathVariable("id") Long id) {
        return organizationService.getOrganizationProfileById(id);
    }

    @GetMapping(value = Routes.ORGANIZATION_ACTIVITIES_BY_PRINCIPAL)
    public List<ActivityResponseDTO> getOrganizationActivitiesByPrincipal(Principal principal) {
        return activityService.getActivitiesByPrincipal(principal);
    }

    @GetMapping(value = Routes.ORGANIZATION_ACTIVITIES_BY_ID)
    public List<ActivityResponseDTO> getActivitiesByOrganizationId(@PathVariable Long id) {
        return activityService.getActivitiesByOrganizationId(id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.ORGANIZATION_PROFILE)
    public void deleteOrganizationProfile(Principal principal) {
        organizationService.deleteOrganizationProfile(principal);
    }
}