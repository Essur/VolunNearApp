package com.volunnear.services.users;

import com.volunnear.dtos.requests.PreferencesRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequestDTO;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.entitiy.infos.Preference;
import com.volunnear.entitiy.infos.Volunteer;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.entitiy.users.AppUser;
import com.volunnear.repositories.infos.PreferenceRepository;
import com.volunnear.repositories.infos.VolunteerPreferenceRepository;
import com.volunnear.repositories.infos.VolunteerRepository;
import com.volunnear.repositories.users.AppUserRepository;
import com.volunnear.services.activities.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
public class VolunteerService {
    private ActivityService activityService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppUserRepository redisRepository;
    private final VolunteerRepository volunteerRepository;
    private final PreferenceRepository preferenceRepository;
    private final VolunteerPreferenceRepository volunteerPreferenceRepository;

    @Lazy
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public ResponseEntity<?> registerVolunteer(RegistrationVolunteerRequestDTO registrationVolunteerRequestDTO) {
        if (redisRepository.existsByUsername(registrationVolunteerRequestDTO.getUsername())) {
            return new ResponseEntity<>("User with username " + registrationVolunteerRequestDTO.getUsername() + " already exists", HttpStatus.OK);
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(registrationVolunteerRequestDTO.getUsername());
        appUser.setPassword(passwordEncoder.encode(registrationVolunteerRequestDTO.getPassword()));
        appUser.setRoles(Set.of("ROLE_VOLUNTEER"));
        redisRepository.save(appUser);

        addAdditionalInfo(registrationVolunteerRequestDTO);
        return new ResponseEntity<>("Successfully registration!", HttpStatus.OK);
    }

    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        Volunteer volunteerByUsername = volunteerRepository.findByUsername(principal.getName()).get();
        List<VolunteerPreference> volunteerPreferences = volunteerPreferenceRepository.findAllByVolunteer_Username(principal.getName());
        List<String> preferences = volunteerPreferences.stream().map(volunteerPreference -> volunteerPreference.getPreference().getName()).toList();

        VolunteerProfileResponseDTO profileResponse = new VolunteerProfileResponseDTO();

        profileResponse.setEmail(volunteerByUsername.getEmail());
        profileResponse.setUsername(volunteerByUsername.getUsername());
        profileResponse.setFirstName(volunteerByUsername.getFirstName());
        profileResponse.setLastName(volunteerByUsername.getLastName());
        if (preferences.isEmpty()) {
            profileResponse.setPreferences(List.of("Preferences is empty"));
        } else profileResponse.setPreferences(preferences);
        profileResponse.setActivitiesDTO(activityService.getActivitiesOfVolunteer(principal));

        return profileResponse;
    }

    public String setPreferencesForVolunteer(PreferencesRequestDTO preferencesRequestDTO, Principal principal) {
        Volunteer volunteer = volunteerRepository.findByUsername(principal.getName()).get();

        for (String preference : preferencesRequestDTO.getPreferences()) {
            if (!preferenceRepository.existsPreferenceByNameIgnoreCase(preference)) {
                Preference newPreference = new Preference();
                newPreference.setName(preference);
                preferenceRepository.save(newPreference);
            }
        }
        List<Preference> preferences = preferenceRepository.findAllByNameIgnoreCaseIn(preferencesRequestDTO.getPreferences());

        List<VolunteerPreference> volunteerPreferences = new ArrayList<>();

        for (Preference preference : preferences) {
            VolunteerPreference volunteerPreference = new VolunteerPreference();
            volunteerPreference.setVolunteer(volunteer);
            volunteerPreference.setPreference(preference);
            volunteerPreferences.add(volunteerPreference);
        }
        volunteerPreferenceRepository.saveAll(volunteerPreferences);
        return "Successfully set your preferences";
    }

    public List<VolunteerPreference> getPreferencesOfUser(Principal principal) {
        return volunteerPreferenceRepository.findAllByVolunteer_Username(principal.getName());
    }

    public Optional<Volunteer> getVolunteerInfo(Principal principal) {
        return volunteerRepository.findByUsername(principal.getName());
    }

    public ResponseEntity<?> updateVolunteerInfo(UpdateVolunteerInfoRequestDTO updateVolunteerInfoRequestDTO, Principal principal) {
        Optional<Volunteer> byUsername = volunteerRepository.findByUsername(principal.getName());
        if (byUsername.isEmpty()) {
            return new ResponseEntity<>("Bad credentials, try re-login", HttpStatus.BAD_REQUEST);
        }
        Volunteer volunteer = byUsername.get();
        volunteer.setEmail(updateVolunteerInfoRequestDTO.getEmail());
        volunteer.setFirstName(updateVolunteerInfoRequestDTO.getFirstName());
        volunteer.setLastName(updateVolunteerInfoRequestDTO.getLastName());
        volunteerRepository.save(volunteer);
        return new ResponseEntity<>("Data was successfully updated", HttpStatus.OK);
    }


    public boolean isUserAreVolunteer(Volunteer volunteer) {
        return volunteerRepository.existsByUsername(volunteer.getUsername());
    }

    private void addAdditionalInfo(RegistrationVolunteerRequestDTO requestDTO) {
        Volunteer volunteer = new Volunteer();
        volunteer.setUsername(requestDTO.getUsername());
        volunteer.setFirstName(requestDTO.getFirstName());
        volunteer.setLastName(requestDTO.getLastName());
        volunteer.setEmail(requestDTO.getEmail());
        volunteerRepository.save(volunteer);
    }

}
