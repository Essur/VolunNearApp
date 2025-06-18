package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.request.profile.OrganizationProfileSaveRequestDTO;
import com.volunnear.dto.response.PagedResponseDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.dto.response.profile.OrganizationProfileResponseDTO;
import com.volunnear.service.activity.ActivityService;
import com.volunnear.service.profile.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public PagedResponseDTO<ActivityResponseDTO> getOrganizationActivitiesByPrincipal(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal) {
        return activityService.getActivitiesByPrincipal(PageRequest.of(page, size), principal);
    }

    @GetMapping(value = Routes.ORGANIZATION_ACTIVITIES_BY_ID)
    public PagedResponseDTO<ActivityResponseDTO> getActivitiesByOrganizationId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @PathVariable Long id) {
        return activityService.getActivitiesByOrganizationId(PageRequest.of(page, size), id);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.ORGANIZATION_PROFILE)
    public void deleteOrganizationProfile(Principal principal) {
        organizationService.deleteOrganizationProfile(principal);
    }
}