package com.volunnear.services.activities;

import com.volunnear.dtos.response.ActivitiesDTO;
import com.volunnear.entitiy.infos.VolunteerPreference;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final ActivityService activityService;
    private final VolunteerService volunteerService;

    public List<ActivitiesDTO> generateRecommendations(Principal principal) {
        List<String> preferences = learnPreferences(principal);
        if (preferences == null) {
            throw new DataNotFoundException("In your profile no preferences set");
        }
        List<ActivitiesDTO> organizationsWithActivitiesByPreferences = activityService.getActivitiesOfOrganizationByPreferences(preferences);
        if (organizationsWithActivitiesByPreferences.isEmpty()) {
            throw new DataNotFoundException("Activities by your preferences not founded");
        }
        return organizationsWithActivitiesByPreferences;
    }

    private List<String> learnPreferences(Principal principal) {
        List<VolunteerPreference> preferencesOfUser = volunteerService.getPreferencesOfUser(principal);
        return preferencesOfUser.stream().map(volunteerPreference -> volunteerPreference.getPreference().getName()).toList();
    }
}
