package com.volunnear.services.users;

import com.volunnear.dtos.requests.RegistrationOrganizationRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganizationInfoRequestDTO;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.entitiy.infos.Organization;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.activities.ActivitiesRepository;
import com.volunnear.repositories.infos.OrganizationRepository;
import com.volunnear.repositories.users.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final OrganizationRepository organizationRepository;

    private final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    public Organization getOrganizationProfile(Principal principal) {
        return organizationRepository.findOrganizationByUsername(principal.getName()).get();
    }

    public List<OrganizationResponseDTO> getAllOrganizationsWithInfo() {
        List<Organization> organizationInfos = organizationRepository.findAll();
        List<OrganizationResponseDTO> organizationResponseDTO = new ArrayList<>();
        for (Organization organizationInfo : organizationInfos) {
            organizationResponseDTO.add(getOrganizationResponseDTO(organizationInfo));
        }
        return organizationResponseDTO;
    }

    public ResponseEntity<?> registerOrganization(RegistrationOrganizationRequestDTO registrationOrganizationRequestDTO) {
        if (appUserRepository.existsByUsername(registrationOrganizationRequestDTO.getUsername())) {
            return new ResponseEntity<>("User with username " + registrationOrganizationRequestDTO.getUsername() + " already exists", HttpStatus.CONFLICT);
        }
        AppUser organization = new AppUser();
        organization.setUsername(registrationOrganizationRequestDTO.getUsername());
        organization.setPassword(passwordEncoder.encode(registrationOrganizationRequestDTO.getPassword()));
        organization.setRoles(Set.of("ORGANIZATION"));
        appUserRepository.save(organization);
        addAdditionalDataAboutOrganization(registrationOrganizationRequestDTO);
        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }

    public ResponseEntity<?> updateOrganizationInfo(UpdateOrganizationInfoRequestDTO updateOrganizationInfoRequestDTO, Principal principal) {
        Optional<Organization> organizationByUsername = organizationRepository.findOrganizationByUsername(principal.getName());
        if (organizationByUsername.isEmpty()) {
            return new ResponseEntity<>("Bad credentials, try re-login", HttpStatus.BAD_REQUEST);
        }
        Organization organization = organizationByUsername.get();
        organization.setEmail(updateOrganizationInfoRequestDTO.getEmail());
        organization.setName(updateOrganizationInfoRequestDTO.getNameOfOrganization());
        organization.setCountry(updateOrganizationInfoRequestDTO.getCountry());
        organization.setCity(updateOrganizationInfoRequestDTO.getCity());
        organization.setAddress(updateOrganizationInfoRequestDTO.getAddress());
        organizationRepository.save(organization);
        return new ResponseEntity<>("Data was successfully updated", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteOrganizationProfile(Principal principal) {
        Optional<Organization> organizationByUsername = organizationRepository.findOrganizationByUsername(principal.getName());
        if (organizationByUsername.isEmpty()) {
            return new ResponseEntity<>("Bad credentials, try re-login", HttpStatus.BAD_REQUEST);
        } else {
            activitiesRepository.deleteAllByOrganization_Id(organizationByUsername.get().getId());
            organizationRepository.delete(organizationByUsername.get());
            appUserRepository.deleteAppUserByUsername(principal.getName());
        }
        return new ResponseEntity<>("Data was successfully deleted", HttpStatus.OK);
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

    private void addAdditionalDataAboutOrganization(RegistrationOrganizationRequestDTO registrationOrganizationRequestDTO) {
        Organization organization = Organization.builder()
                .name(registrationOrganizationRequestDTO.getNameOfOrganization())
                .username(registrationOrganizationRequestDTO.getUsername())
                .email(registrationOrganizationRequestDTO.getEmail())
                .country(registrationOrganizationRequestDTO.getCountry())
                .city(registrationOrganizationRequestDTO.getCity())
                .address(registrationOrganizationRequestDTO.getAddress())
                .build();
        logger.info("------------------------------------");
        logger.info(organization.toString());
        logger.info("------------------------------------");
        organizationRepository.save(organization);
    }
}
