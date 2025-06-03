package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.request.profile.OrganizationProfileSaveRequestDTO;
import com.volunnear.dto.response.profile.OrganizationProfileResponseDTO;
import com.volunnear.service.profile.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping(value = Routes.ORGANIZATION_PROFILE)
    public OrganizationProfileResponseDTO createOrganizationProfile(@RequestBody @Valid OrganizationProfileSaveRequestDTO requestDTO, Principal principal) {
        return organizationService.createOrganizationProfile(requestDTO, principal);
    }

    @PutMapping(value = Routes.ORGANIZATION_PROFILE)
    public OrganizationProfileResponseDTO updateOrganizationProfile(@RequestBody @Valid OrganizationProfileSaveRequestDTO requestDTO, Principal principal) {
        return organizationService.updateOrganizationProfile(requestDTO, principal);
    }

    @GetMapping(value = Routes.ORGANIZATION_PROFILE)
    public OrganizationProfileResponseDTO getOrganizationProfile(Principal principal) {
        return organizationService.getOrganizationProfile(principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.ORGANIZATION_PROFILE)
    public void deleteOrganizationProfile(Principal principal) {
        organizationService.deleteOrganizationProfile(principal);
    }
}