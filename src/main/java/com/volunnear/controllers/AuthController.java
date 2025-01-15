package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.jwt.JwtResponse;
import com.volunnear.dtos.requests.RegistrationOrganizationRequest;
import com.volunnear.dtos.requests.RegistrationVolunteerRequest;
import com.volunnear.dtos.requests.UpdateOrganizationInfoRequest;
import com.volunnear.dtos.requests.UpdateVolunteerInfoRequest;
import com.volunnear.dtos.response.OrganizationResponseDTO;
import com.volunnear.dtos.response.VolunteerProfileResponseDTO;
import com.volunnear.services.security.AuthService;
import com.volunnear.services.users.OrganizationService;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final VolunteerService volunteerService;
    private final OrganizationService organizationService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.LOGIN)
    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = Routes.REGISTER_VOLUNTEER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer registrationOfVolunteer(@RequestBody RegistrationVolunteerRequest registrationVolunteerRequest) {
        return volunteerService.registerVolunteer(registrationVolunteerRequest);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = Routes.REGISTER_ORGANIZATION, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer registrationOfOrganization(@RequestBody RegistrationOrganizationRequest registrationOrganizationRequest) {
        return organizationService.registerOrganization(registrationOrganizationRequest);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = Routes.UPDATE_VOLUNTEER_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public VolunteerProfileResponseDTO updateVolunteerInfo(@RequestBody UpdateVolunteerInfoRequest updateVolunteerInfoRequest, Principal principal) {
        return volunteerService.updateVolunteerInfo(updateVolunteerInfoRequest, principal);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = Routes.UPDATE_ORGANIZATION_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OrganizationResponseDTO updateOrganizationInfo(@RequestBody UpdateOrganizationInfoRequest updateOrganizationInfoRequest, Principal principal) {
        return organizationService.updateOrganizationInfo(updateOrganizationInfoRequest, principal);
    }
}
