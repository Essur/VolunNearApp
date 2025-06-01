package com.volunnear.service.profile;

import com.volunnear.dto.request.profile.VolunteerProfileSaveRequestDTO;
import com.volunnear.dto.response.profile.VolunteerProfileResponseDTO;
import com.volunnear.entity.profile.VolunteerProfile;
import com.volunnear.entity.users.AppUser;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.mapper.profile.VolunteerProfileMapper;
import com.volunnear.repository.profile.VolunteerProfileRepository;
import com.volunnear.service.user.UserService;
import com.volunnear.util.PrincipalUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VolunteerService {
    private final UserService userService;
    private final PrincipalUtils principalUtils;
    private final VolunteerProfileRepository volunteerProfileRepository;

    public VolunteerProfileResponseDTO createVolunteerProfile(VolunteerProfileSaveRequestDTO createRequest, Principal principal) {
        AppUser appUser = principalUtils.getUserFromPrincipal(principal);
        if (volunteerProfileRepository.existsByAppUser_Username(appUser.getUsername())) {
            throw new UserAlreadyExistsException("Volunteer profile with username " + appUser.getUsername() + " already exists, try update profile");
        }
        VolunteerProfile volunteerProfile = VolunteerProfileMapper.mapper.toEntity(createRequest, appUser);
        volunteerProfileRepository.save(volunteerProfile);
        return VolunteerProfileMapper.mapper.toDto(volunteerProfile);
    }

    public VolunteerProfileResponseDTO updateVolunteerProfile(VolunteerProfileSaveRequestDTO editRequest, Principal principal) {
        AppUser appUser = principalUtils.getUserFromPrincipal(principal);
        VolunteerProfile profile = volunteerProfileRepository.findByAppUser_Username(appUser.getUsername())
                .orElseThrow(() -> new DataNotFoundException("Volunteer profile with username " + appUser.getUsername() + " not found"));

        VolunteerProfileMapper.mapper.updateVolunteerProfileFromDto(editRequest, profile);
        volunteerProfileRepository.save(profile);
        return VolunteerProfileMapper.mapper.toDto(profile);
    }

    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        VolunteerProfile profile = volunteerProfileRepository.findByAppUser_Username(principal.getName())
                .orElseThrow(() -> new BadUserCredentialsException("User with username" + principal.getName() + " not found"));
        return VolunteerProfileMapper.mapper.toDto(profile);
    }

    public void deleteVolunteerProfile(Principal principal) {
        if (!volunteerProfileRepository.existsByAppUser_Username(principal.getName())) {
            throw new DataNotFoundException("Volunteer profile with username " + principal.getName() + " not found");
        }
        userService.deleteAppUser(principal.getName());
    }
}
