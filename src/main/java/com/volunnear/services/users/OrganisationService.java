package com.volunnear.services.users;

import com.volunnear.dtos.requests.RegistrationOrganisationRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganisationInfoRequestDTO;
import com.volunnear.dtos.response.OrganisationResponseDTO;
import com.volunnear.entitiy.infos.Organisation;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.infos.OrganisationRepository;
import com.volunnear.repositories.users.AppUserRepository;
import lombok.RequiredArgsConstructor;
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
public class OrganisationService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OrganisationRepository organisationRepository;

    public Organisation getOrganisationProfile(Principal principal) {
        return organisationRepository.findOrganisationByUsername(principal.getName()).get();
    }

    public List<OrganisationResponseDTO> getAllOrganisationsWithInfo() {
        List<Organisation> organisationInfos = organisationRepository.findAll();
        List<OrganisationResponseDTO> organisationResponseDTO = new ArrayList<>();
        for (Organisation organisationInfo : organisationInfos) {
            organisationResponseDTO.add(getOrganisationResponseDTO(organisationInfo));
        }
        return organisationResponseDTO;
    }

    public ResponseEntity<?> registerOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        if (appUserRepository.existsByUsername(registrationOrganisationRequestDTO.getUsername())) {
            return new ResponseEntity<>("User with username " + registrationOrganisationRequestDTO.getUsername() + " already exists", HttpStatus.OK);
        }
        AppUser organisation = new AppUser();
        organisation.setUsername(registrationOrganisationRequestDTO.getUsername());
        organisation.setPassword(passwordEncoder.encode(registrationOrganisationRequestDTO.getPassword()));
        organisation.setRoles(Set.of("ORGANISATION"));
        appUserRepository.save(organisation);
        addAdditionalDataAboutOrganisation(registrationOrganisationRequestDTO);
        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }

    private void addAdditionalDataAboutOrganisation(RegistrationOrganisationRequestDTO registrationOrganisationRequestDTO) {
        Organisation organisation = Organisation.builder()
                .name(registrationOrganisationRequestDTO.getNameOfOrganisation())
                .username(registrationOrganisationRequestDTO.getUsername())
                .email(registrationOrganisationRequestDTO.getEmail())
                .country(registrationOrganisationRequestDTO.getCountry())
                .city(registrationOrganisationRequestDTO.getCity())
                .address(registrationOrganisationRequestDTO.getAddress())
                .build();
        organisationRepository.save(organisation);
    }

    public ResponseEntity<?> updateOrganisationInfo(UpdateOrganisationInfoRequestDTO updateOrganisationInfoRequestDTO, Principal principal) {
        Optional<Organisation> organisationByUsername = organisationRepository.findOrganisationByUsername(principal.getName());
        if (organisationByUsername.isEmpty()) {
            return new ResponseEntity<>("Bad credentials, try re-login", HttpStatus.BAD_REQUEST);
        }
        Organisation organisation = organisationByUsername.get();
        organisation.setEmail(updateOrganisationInfoRequestDTO.getEmail());
        organisation.setName(updateOrganisationInfoRequestDTO.getNameOfOrganisation());
        organisation.setCountry(updateOrganisationInfoRequestDTO.getCountry());
        organisation.setCity(updateOrganisationInfoRequestDTO.getCity());
        organisation.setAddress(updateOrganisationInfoRequestDTO.getAddress());
        organisationRepository.save(organisation);
        return new ResponseEntity<>("Data was successfully updated", HttpStatus.OK);
    }

    public Optional<Organisation> findOrganisationByUsername(String username) {
        return organisationRepository.findOrganisationByUsername(username);
    }

    public Optional<Organisation> findOrganisationById(Integer id) {
        return organisationRepository.findById(id);
    }

    public Optional<Organisation> findOrganisationByNameOfOrganisation(String nameOfOrganisation) {
        return organisationRepository.findOrganisationByNameIgnoreCase(nameOfOrganisation);
    }

    public boolean isUserAreOrganisation(String username) {
        return organisationRepository.existsByUsername(username);
    }

    public OrganisationResponseDTO getResponseDTOForSubscriptions(Organisation organisation) {
        return getOrganisationResponseDTO(organisation);
    }

    public OrganisationResponseDTO getOrganisationResponseDTO(Organisation organisation) {
        return new OrganisationResponseDTO(
                organisation.getId(),
                organisation.getName(),
                organisation.getCountry(),
                organisation.getCity(),
                organisation.getAddress(),
                organisation.getEmail());
    }

}
