package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.requests.RegistrationOrganizationRequestDTO;
import com.volunnear.dtos.requests.RegistrationVolunteerRequestDTO;
import com.volunnear.dtos.requests.UpdateOrganizationInfoRequestDTO;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequestDTO;
import com.volunnear.services.security.AuthService;
import com.volunnear.services.users.OrganizationService;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final VolunteerService volunteerService;
    private final OrganizationService organizationService;

    @PostMapping(value = Routes.LOGIN)
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping(value = Routes.REGISTER_VOLUNTEER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationOfVolunteer(@RequestBody RegistrationVolunteerRequestDTO registrationVolunteerRequestDto) {
        return volunteerService.registerVolunteer(registrationVolunteerRequestDto);
    }

    @PostMapping(value = Routes.REGISTER_ORGANIZATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registrationOfOrganization(@RequestBody RegistrationOrganizationRequestDTO registrationOrganizationRequestDTO) {
        return organizationService.registerOrganization(registrationOrganizationRequestDTO);
    }

    @PutMapping(value = Routes.UPDATE_VOLUNTEER_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateVolunteerInfo(@RequestBody UpdateVolunteerInfoRequestDTO updateVolunteerInfoRequestDTO, Principal principal) {
        return volunteerService.updateVolunteerInfo(updateVolunteerInfoRequestDTO, principal);
    }

    @PutMapping(value = Routes.UPDATE_ORGANIZATION_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateOrganizationInfo(@RequestBody UpdateOrganizationInfoRequestDTO updateOrganizationInfoRequest, Principal principal) {
        return organizationService.updateOrganizationInfo(updateOrganizationInfoRequest, principal);
    }
}
