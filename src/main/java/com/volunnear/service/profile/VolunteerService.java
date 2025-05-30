package com.volunnear.service.profile;

import com.volunnear.dto.request.profile.CreateVolunteerProfileInfoRequestDTO;
import com.volunnear.dto.response.profile.VolunteerProfileResponseDTO;
import com.volunnear.entity.profile.VolunteerProfile;
import com.volunnear.entity.users.AppUser;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.mapper.profile.VolunteerProfileMapper;
import com.volunnear.repository.profile.VolunteerProfileRepository;
import com.volunnear.service.user.UserService;
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
    private final VolunteerProfileRepository volunteerProfileRepository;

    public VolunteerProfileResponseDTO createVolunteerProfile(CreateVolunteerProfileInfoRequestDTO editRequest, Principal principal) {
        AppUser appUser = userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new BadUserCredentialsException("User with username" + principal.getName() + " not found"));

        VolunteerProfile volunteerProfile = VolunteerProfileMapper.mapper.toEntity(editRequest, appUser);
        volunteerProfileRepository.save(volunteerProfile);
        return VolunteerProfileMapper.mapper.toDto(volunteerProfile);
    }
}
