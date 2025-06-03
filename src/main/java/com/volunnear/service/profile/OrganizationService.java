package com.volunnear.service.profile;

import com.volunnear.dto.request.profile.OrganizationProfileSaveRequestDTO;
import com.volunnear.dto.response.profile.OrganizationProfileResponseDTO;
import com.volunnear.entity.profile.OrganizationProfile;
import com.volunnear.entity.users.AppUser;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.mapper.profile.OrganizationProfileMapper;
import com.volunnear.repository.profile.OrganizationProfileRepository;
import com.volunnear.util.AppUserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrganizationService {
    private final AppUserUtils appUserUtils;
    private final OrganizationProfileMapper organizationProfileMapper;
    private final OrganizationProfileRepository organizationProfileRepository;

    public OrganizationProfileResponseDTO createOrganizationProfile(OrganizationProfileSaveRequestDTO createRequest, Principal principal) {
        AppUser appUser = appUserUtils.getUserFromPrincipal(principal);
        if (appUserUtils.hasOrganizationProfile(appUser)) {
            throw new UserAlreadyExistsException("Organization profile with username " + appUser.getUsername() + " already exists, try update profile");
        }
        OrganizationProfile organizationProfile = organizationProfileMapper.toEntity(createRequest, appUser);
        organizationProfileRepository.save(organizationProfile);
        return organizationProfileMapper.toDto(organizationProfile);
    }

    public OrganizationProfileResponseDTO updateOrganizationProfile(OrganizationProfileSaveRequestDTO editRequest, Principal principal) {
        AppUser appUser = appUserUtils.getUserFromPrincipal(principal);
        OrganizationProfile profile = organizationProfileRepository.findByAppUser_Username(appUser.getUsername())
                .orElseThrow(() -> new DataNotFoundException("Volunteer profile with username " + appUser.getUsername() + " not found"));
        organizationProfileMapper.updateEntity(editRequest, profile);
        organizationProfileRepository.save(profile);
        return organizationProfileMapper.toDto(profile);
    }

    public OrganizationProfileResponseDTO getOrganizationProfile(Principal principal) {
        AppUser appUser = appUserUtils.getUserFromPrincipal(principal);
        OrganizationProfile profile = organizationProfileRepository.findByAppUser_Username(appUser.getUsername())
                .orElseThrow(() -> new DataNotFoundException("Volunteer profile with username " + appUser.getUsername() + " not found"));
        return organizationProfileMapper.toDto(profile);
    }

    public void deleteOrganizationProfile(Principal principal) {
        if (!organizationProfileRepository.existsByAppUser_Username(principal.getName())) {
            throw new DataNotFoundException("Volunteer profile with username " + principal.getName() + " not found");
        }
        appUserUtils.deleteAppUserByPrincipal(principal);
    }
}
