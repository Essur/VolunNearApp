package com.volunnear.services.users;

import com.volunnear.dtos.requests.RegistrationVolunteerRequest;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequest;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.entity.infos.Volunteer;
import com.volunnear.entity.users.AppUser;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.repositories.infos.VolunteerRepository;
import com.volunnear.repositories.users.AppUserRepository;
import com.volunnear.repositories.users.RefreshTokenRepository;
import com.volunnear.services.activities.ActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VolunteerService {
    private ActivityService activityService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final VolunteerRepository volunteerRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Lazy
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public Integer registerVolunteer(RegistrationVolunteerRequest registrationVolunteerRequest) {
        if (appUserRepository.existsByUsername(registrationVolunteerRequest.getUsername())) {
            throw new UserAlreadyExistsException("User with username " + registrationVolunteerRequest.getUsername() + " already exists");
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(registrationVolunteerRequest.getUsername());
        appUser.setPassword(passwordEncoder.encode(registrationVolunteerRequest.getPassword()));
        appUser.setRoles(Set.of("VOLUNTEER"));
        appUserRepository.save(appUser);

        addAdditionalInfo(registrationVolunteerRequest, appUser);
        return appUser.getId();
    }

    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        Volunteer volunteerByUsername = volunteerRepository.findByUser_Username(principal.getName()).get();

        VolunteerProfileResponseDTO profileResponse = new VolunteerProfileResponseDTO();

        profileResponse.setEmail(volunteerByUsername.getEmail());
        profileResponse.setUsername(volunteerByUsername.getUser().getUsername());
        profileResponse.setFirstName(volunteerByUsername.getFirstName());
        profileResponse.setLastName(volunteerByUsername.getLastName());
        profileResponse.setActivitiesDTO(activityService.getActivitiesOfVolunteer(principal));

        return profileResponse;
    }


    public Optional<Volunteer> getVolunteerInfo(Principal principal) {
        return volunteerRepository.findByUser_Username(principal.getName());
    }

    public VolunteerProfileResponseDTO updateVolunteerInfo(UpdateVolunteerInfoRequest updateVolunteerInfoRequest, Principal principal) {
        Optional<Volunteer> byUsername = volunteerRepository.findByUser_Username(principal.getName());
        if (byUsername.isEmpty()) {
            throw new BadUserCredentialsException("Bad credentials, try re-login");
        }
        Volunteer volunteer = byUsername.get();
        volunteer.setEmail(updateVolunteerInfoRequest.getEmail());
        volunteer.setFirstName(updateVolunteerInfoRequest.getFirstName());
        volunteer.setLastName(updateVolunteerInfoRequest.getLastName());
        volunteerRepository.save(volunteer);

        return getVolunteerProfile(principal);
    }

    @Transactional
    public void deleteVolunteerProfile(Principal principal) {
        Optional<Volunteer> volunteer = volunteerRepository.findByUser_Username(principal.getName());
        if (volunteer.isPresent()) {
            refreshTokenRepository.deleteByAppUser_Username(principal.getName());
            volunteerRepository.delete(volunteer.get());
            appUserRepository.deleteAppUserByUsername(principal.getName());
        } else {
            throw new BadUserCredentialsException("Bad credentials, try re-login");
        }
    }

    public boolean isVolunteerExist (String username) {
        return volunteerRepository.existsByUser_Username(username);
    }

    private void addAdditionalInfo(RegistrationVolunteerRequest requestDTO, AppUser appUser) {
        Volunteer volunteer = new Volunteer();
        volunteer.setUser(appUser);
        volunteer.setFirstName(requestDTO.getFirstName());
        volunteer.setLastName(requestDTO.getLastName());
        volunteer.setEmail(requestDTO.getEmail());
        volunteerRepository.save(volunteer);
    }

}
