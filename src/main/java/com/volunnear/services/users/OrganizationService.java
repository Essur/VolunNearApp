package com.volunnear.services.users;

import com.volunnear.dtos.requests.RegistrationOrganizationRequest;
import com.volunnear.dtos.requests.UpdateOrganizationInfoRequest;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.repositories.activities.ActivitiesRepository;
import com.volunnear.repositories.infos.OrganizationRepository;
import com.volunnear.repositories.users.AppUserRepository;
import com.volunnear.repositories.users.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ActivitiesRepository activitiesRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OrganizationRepository organizationRepository;

    private final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    public OrganizationResponseDTO getOrganizationProfile(Principal principal) {
        Optional<Organization> organizationByUsername = organizationRepository.findOrganizationByUsername(principal.getName());
        if (organizationByUsername.isEmpty()) {
            throw new DataNotFoundException("Organization profile not found");
        }
        return getOrganizationResponseDTO(organizationByUsername.get());
    }

    public List<OrganizationResponseDTO> getAllOrganizationsWithInfo() {
        List<Organization> organizationInfos = organizationRepository.findAll();
        List<OrganizationResponseDTO> organizationResponseDTO = new ArrayList<>();
        for (Organization organizationInfo : organizationInfos) {
            organizationResponseDTO.add(getOrganizationResponseDTO(organizationInfo));
        }
        return organizationResponseDTO;
    }

    public Integer registerOrganization(RegistrationOrganizationRequest registrationOrganizationRequest) {
        if (appUserRepository.existsByUsername(registrationOrganizationRequest.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + registrationOrganizationRequest.getUsername() + " already exists");
        }
        AppUser organization = new AppUser();
        organization.setUsername(registrationOrganizationRequest.getUsername());
        organization.setPassword(passwordEncoder.encode(registrationOrganizationRequest.getPassword()));
        organization.setRoles(Set.of("ORGANIZATION"));
        appUserRepository.save(organization);
        addAdditionalDataAboutOrganization(registrationOrganizationRequest);
        return organization.getId();
    }

    public OrganizationResponseDTO updateOrganizationInfo(UpdateOrganizationInfoRequest updateOrganizationInfoRequest, Principal principal) {
        Optional<Organization> organizationByUsername = organizationRepository.findOrganizationByUsername(principal.getName());
        if (organizationByUsername.isEmpty()) {
            throw new BadUserCredentialsException("Bad credentials, try re-login");
        }
        Organization organization = organizationByUsername.get();
        organization.setEmail(updateOrganizationInfoRequest.getEmail());
        organization.setName(updateOrganizationInfoRequest.getNameOfOrganization());
        organization.setCountry(updateOrganizationInfoRequest.getCountry());
        organization.setCity(updateOrganizationInfoRequest.getCity());
        organization.setAddress(updateOrganizationInfoRequest.getAddress());
        organizationRepository.save(organization);
        return getOrganizationResponseDTO(organization);
    }

    @Transactional
    public void deleteOrganizationProfile(Principal principal) {
        Optional<Organization> organizationByUsername = organizationRepository.findOrganizationByUsername(principal.getName());
        if (organizationByUsername.isEmpty()) {
            throw new BadUserCredentialsException("Bad credentials, try re-login");
        } else {
            refreshTokenRepository.deleteByAppUser_Username(principal.getName());
            activitiesRepository.deleteAllByOrganization_Id(organizationByUsername.get().getId());
            organizationRepository.delete(organizationByUsername.get());
            appUserRepository.deleteAppUserByUsername(principal.getName());
        }
    }

    public Integer getOrganizationId(Principal principal) {
        Optional<Organization> organizationByUsername = findOrganizationByUsername(principal.getName());
        if (organizationByUsername.isEmpty()) {
            throw new BadUserCredentialsException("Bad credentials, try re-login");
        }
        return organizationByUsername.get().getId();
    }

    public Optional<Organization> findOrganizationByUsername(String username) {
        return organizationRepository.findOrganizationByUsername(username);
    }

    public Optional<Organization> findOrganizationById(Integer id) {
        return organizationRepository.findById(id);
    }

    public Optional<Organization> findOrganizationByNameOfOrganization(String nameOfOrganization) {
        return organizationRepository.findOrganizationByNameIgnoreCase(nameOfOrganization);
    }

    public boolean isUserAreOrganization(String username) {
        return organizationRepository.existsByUsername(username);
    }

    public OrganizationResponseDTO getResponseDTOForSubscriptions(Organization organization) {
        return getOrganizationResponseDTO(organization);
    }

    public OrganizationResponseDTO getOrganizationResponseDTO(Organization organization) {
        return new OrganizationResponseDTO(
                organization.getId(),
                organization.getName(),
                organization.getCountry(),
                organization.getCity(),
                organization.getAddress(),
                organization.getEmail());
    }

    private void addAdditionalDataAboutOrganization(RegistrationOrganizationRequest registrationOrganizationRequest) {
        Organization organization = Organization.builder()
                .name(registrationOrganizationRequest.getNameOfOrganization())
                .username(registrationOrganizationRequest.getUsername())
                .email(registrationOrganizationRequest.getEmail())
                .country(registrationOrganizationRequest.getCountry())
                .city(registrationOrganizationRequest.getCity())
                .address(registrationOrganizationRequest.getAddress())
                .build();
        logger.info("------------------------------------");
        logger.info(organization.toString());
        logger.info("------------------------------------");
        organizationRepository.save(organization);
    }


}
