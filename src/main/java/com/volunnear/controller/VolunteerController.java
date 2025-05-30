package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.request.profile.CreateVolunteerProfileInfoRequestDTO;
import com.volunnear.dto.response.profile.VolunteerProfileResponseDTO;
import com.volunnear.service.profile.VolunteerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;

    @PostMapping(value = Routes.CREATE_VOLUNTEER_PROFILE)
    public VolunteerProfileResponseDTO createVolunteerProfile(@Valid @RequestBody CreateVolunteerProfileInfoRequestDTO requestDTO, Principal principal) {
        return volunteerService.createVolunteerProfile(requestDTO, principal);
    }
}
