package com.volunnear.services.users;

import com.volunnear.dtos.requests.DeletePreferenceFromVolunteerProfileRequest;
import com.volunnear.dtos.requests.PreferencesRequest;
import com.volunnear.dtos.requests.RegistrationVolunteerRequest;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequest;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.entitiy.infos.Preference;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.infos.VolunteerPreferenceId;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.repositories.infos.PreferenceRepository;
import com.volunnear.repositories.infos.VolunteerPreferenceRepository;
import com.volunnear.repositories.infos.VolunteerRepository;
import com.volunnear.repositories.users.AppUserRepository;
import com.volunnear.repositories.users.RefreshTokenRepository;
import com.volunnear.services.activities.ActivityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VolunteerService {
    private ActivityService activityService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final VolunteerRepository volunteerRepository;
    private final PreferenceRepository preferenceRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final VolunteerPreferenceRepository volunteerPreferenceRepository;
    private final Logger logger = LoggerFactory.getLogger(VolunteerService.class);

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

        addAdditionalInfo(registrationVolunteerRequest);
        return appUser.getId();
    }

    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        Volunteer volunteerByUsername = volunteerRepository.findByUsername(principal.getName()).get();
        List<VolunteerPreference> volunteerPreferences = volunteerPreferenceRepository.findAllByVolunteer_Username(principal.getName());
        List<VolunteerPreferenceDTO> preferences = new ArrayList<>();
        for (VolunteerPreference volunteerPreference :volunteerPreferences) {
            preferences.add(new VolunteerPreferenceDTO(
                    volunteerPreference.getId().getPreferenceId(),
                    volunteerPreference.getId().getVolunteerId(),
                    volunteerPreference.getPreference().getName()));
        }

        VolunteerProfileResponseDTO profileResponse = new VolunteerProfileResponseDTO();

        profileResponse.setEmail(volunteerByUsername.getEmail());
        profileResponse.setUsername(volunteerByUsername.getUsername());
        profileResponse.setFirstName(volunteerByUsername.getFirstName());
        profileResponse.setLastName(volunteerByUsername.getLastName());
        if (!preferences.isEmpty()) {
            profileResponse.setPreferences(preferences);

        } else {
            profileResponse.setPreferences(new ArrayList<>());
        }
        profileResponse.setActivitiesDTO(activityService.getActivitiesOfVolunteer(principal));

        return profileResponse;
    }

    public VolunteerProfileResponseDTO setPreferencesForVolunteer(PreferencesRequest preferencesRequest, Principal principal) {
        Volunteer volunteer = volunteerRepository.findByUsername(principal.getName()).get();

        for (String preference : preferencesRequest.getPreferences()) {
            if (!preferenceRepository.existsPreferenceByNameIgnoreCase(preference)) {
                Preference newPreference = new Preference();
                newPreference.setName(preference);
                preferenceRepository.save(newPreference);
            }
        }
        List<Preference> preferences = preferenceRepository.findAllByNameIgnoreCaseIn(preferencesRequest.getPreferences());

        List<VolunteerPreference> volunteerPreferences = new ArrayList<>();

        for (Preference preference : preferences) {
            VolunteerPreference volunteerPreference = new VolunteerPreference();
            volunteerPreference.setId(new VolunteerPreferenceId(volunteer.getId(), preference.getId()));
            volunteerPreference.setVolunteer(volunteer);
            volunteerPreference.setPreference(preference);
            volunteerPreferences.add(volunteerPreference);
        }
        volunteerPreferenceRepository.saveAll(volunteerPreferences);
        return getVolunteerProfile(principal);
    }

    public List<VolunteerPreference> getPreferencesOfUser(Principal principal) {
        return volunteerPreferenceRepository.findAllByVolunteer_Username(principal.getName());
    }

    @Transactional
    public void deletePreferenceById(DeletePreferenceFromVolunteerProfileRequest preferenceId, Principal principal) {
        Optional<Preference> preferenceById = preferenceRepository.findPreferenceById(preferenceId.getPreferenceId());

        Volunteer volunteer = volunteerRepository.findByUsername(principal.getName()).get();
        logger.info(volunteer.toString());
        logger.info(preferenceById.toString());

        if (preferenceById.isEmpty()) {
            throw new DataNotFoundException("Preference with id " + preferenceId.getPreferenceId() + " not found");
        }
        volunteerPreferenceRepository.deleteVolunteerPreferenceByPreference_IdAndVolunteer_Id(preferenceId.getPreferenceId(), volunteer.getId());
    }

    public Optional<Volunteer> getVolunteerInfo(Principal principal) {
        return volunteerRepository.findByUsername(principal.getName());
    }

    public VolunteerProfileResponseDTO updateVolunteerInfo(UpdateVolunteerInfoRequest updateVolunteerInfoRequest, Principal principal) {
        Optional<Volunteer> byUsername = volunteerRepository.findByUsername(principal.getName());
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
        Optional<Volunteer> volunteer = volunteerRepository.findByUsername(principal.getName());
        if (volunteer.isPresent()) {
            refreshTokenRepository.deleteByAppUser_Username(principal.getName());
            volunteerPreferenceRepository.deleteAllByVolunteer_Username(volunteer.get().getUsername());
            volunteerRepository.delete(volunteer.get());
            appUserRepository.deleteAppUserByUsername(principal.getName());
        } else {
            throw new BadUserCredentialsException("Bad credentials, try re-login");
        }
    }

    public boolean isUserAreVolunteer(Volunteer volunteer) {
        return volunteerRepository.existsByUsername(volunteer.getUsername());
    }

    private void addAdditionalInfo(RegistrationVolunteerRequest requestDTO) {
        Volunteer volunteer = new Volunteer();
        volunteer.setUsername(requestDTO.getUsername());
        volunteer.setFirstName(requestDTO.getFirstName());
        volunteer.setLastName(requestDTO.getLastName());
        volunteer.setEmail(requestDTO.getEmail());
        volunteerRepository.save(volunteer);
    }

}
