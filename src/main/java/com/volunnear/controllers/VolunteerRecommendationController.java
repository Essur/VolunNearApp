package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.requests.AddPreferenceRequest;
import com.volunnear.services.VolunteerRecommendationService;
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
