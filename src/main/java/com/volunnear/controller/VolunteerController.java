package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.request.profile.VolunteerProfileSaveRequestDTO;
import com.volunnear.dto.response.profile.VolunteerProfileResponseDTO;
import com.volunnear.service.profile.VolunteerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;

    @PostMapping(value = Routes.CREATE_VOLUNTEER_PROFILE)
    public VolunteerProfileResponseDTO createVolunteerProfile(@Valid @RequestBody VolunteerProfileSaveRequestDTO createRequest, Principal principal) {
        return volunteerService.createVolunteerProfile(createRequest, principal);
    }

    @PutMapping(value = Routes.UPDATE_VOLUNTEER_PROFILE)
    public VolunteerProfileResponseDTO updateVolunteerProfile(@Valid @RequestBody VolunteerProfileSaveRequestDTO updateRequest, Principal principal) {
        return volunteerService.updateVolunteerProfile(updateRequest, principal);
    }

    @GetMapping(value = Routes.GET_VOLUNTEER_PROFILE)
    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        return volunteerService.getVolunteerProfile(principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.DELETE_VOLUNTEER_PROFILE)
    public void deleteVolunteerProfile(Principal principal) {
        volunteerService.deleteVolunteerProfile(principal);
    }
}
