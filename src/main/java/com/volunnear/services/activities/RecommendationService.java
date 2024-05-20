package com.volunnear.services.activities;

import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final ActivityService activityService;
    private final VolunteerService volunteerService;

    public ResponseEntity<?> generateRecommendations(Principal principal) {
        List<String> preferences = learnPreferences(principal);
        if (preferences == null) {
            return new ResponseEntity<>("In your profile no preferences set", HttpStatus.BAD_REQUEST);
        }
        List<ActivitiesDTO> organizationsWithActivitiesByPreferences = activityService.getActivitiesOfOrganizationByPreferences(preferences);
        if (organizationsWithActivitiesByPreferences.isEmpty()) {
            return new ResponseEntity<>("Activities by your preferences not founded", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(organizationsWithActivitiesByPreferences, HttpStatus.OK);
    }

    private List<String> learnPreferences(Principal principal) {
        List<VolunteerPreference> preferencesOfUser = volunteerService.getPreferencesOfUser(principal);
        return preferencesOfUser.stream().map(volunteerPreference -> volunteerPreference.getPreference().getName()).toList();
    }
}
