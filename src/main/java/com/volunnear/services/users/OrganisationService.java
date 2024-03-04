package com.volunnear.services.users;

import com.volunnear.dtos.OrganisationDTO;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.OrganisationInfo;
import com.volunnear.repositories.users.OrganisationInfoRepository;
import com.volunnear.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganisationService {
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OrganisationInfoRepository organisationInfoRepository;

    public void registerOrganisation(OrganisationDTO organisationDTO) {
        AppUser organisation = new AppUser();
        organisation.setUsername(organisationDTO.getCredentials().getUsername());
        organisation.setPassword(passwordEncoder.encode(organisationDTO.getCredentials().getPassword()));
        organisation.setRoles(roleService.getRoleByName("ROLE_ORGANISATION"));
        organisation.setEmail(organisationDTO.getCredentials().getEmail());
        addAdditionalDataAboutOrganisation(organisation, organisationDTO);
        userRepository.save(organisation);
    }

    private void addAdditionalDataAboutOrganisation(AppUser organisation, OrganisationDTO organisationDTO) {
        OrganisationInfo organisationInfo = new OrganisationInfo();
        organisationInfo.setAppUser(organisation);
        organisationInfo.setNameOfOrganisation(organisationDTO.getNameOfOrganisation());
        organisationInfo.setCountry(organisationDTO.getCountry());
        organisationInfo.setCity(organisationDTO.getCity());
        organisationInfo.setAddress(organisationDTO.getAddress());
        organisationInfo.setIndustry(organisationDTO.getIndustry());
        organisationInfoRepository.save(organisationInfo);
    }

    public Optional<AppUser> findOrganisationByUsername(String username) {
        return userRepository.findAppUserByUsername(username);
    }

    public Optional<OrganisationInfo> findOrganisationByNameOfOrganisation(String nameOfOrganisation) {
        return organisationInfoRepository.findOrganisationInfoByNameOfOrganisation(nameOfOrganisation);
    }

    public OrganisationInfo findAdditionalInfoAboutOrganisation(AppUser user) {
        return organisationInfoRepository.findOrganisationInfoByAppUser(user);
    }
}
