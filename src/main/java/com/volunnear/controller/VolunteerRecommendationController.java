package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.requests.AddPreferenceRequest;
import com.volunnear.service.VolunteerRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class VolunteerRecommendationController {
    private final VolunteerRecommendationService volunteerRecommendationService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.ADD_PREFERENCE_TO_VOLUNTEER)
    public Integer addVolunteerPreference(@RequestBody AddPreferenceRequest preferenceRequest, Principal principal) {
        return volunteerRecommendationService.addVolunteerPreferences(preferenceRequest, principal);
    }
}
