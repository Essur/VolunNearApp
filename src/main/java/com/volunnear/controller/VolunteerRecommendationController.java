package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.VolunteerPreferenceDTO;
import com.volunnear.dto.requests.AddPreferenceRequest;
import com.volunnear.dto.response.ActivitiesDTO;
import com.volunnear.service.VolunteerRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VolunteerRecommendationController {
    private final VolunteerRecommendationService volunteerRecommendationService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.ADD_PREFERENCE_TO_VOLUNTEER)
    public Integer addVolunteerPreference(@RequestBody AddPreferenceRequest preferenceRequest, Principal principal) {
        return volunteerRecommendationService.addVolunteerPreferences(preferenceRequest, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = Routes.UPDATE_PREFERENCE_TO_VOLUNTEER)
    public VolunteerPreferenceDTO updateVolunteerPreference (@RequestBody AddPreferenceRequest preferenceRequest, Principal principal) {
        return volunteerRecommendationService.updateVolunteerPreferences(preferenceRequest, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = Routes.GET_RECOMMENDATION_BY_PREFERENCES)
    public List<ActivitiesDTO> getRecommendationsByPreferences(@RequestParam Integer range,Principal principal) {
        return volunteerRecommendationService.getRecommendationsForUser(range, principal);
    }
}
