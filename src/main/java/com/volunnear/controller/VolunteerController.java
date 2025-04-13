package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.requests.RegistrationVolunteerRequest;
import com.volunnear.dto.requests.UpdateVolunteerInfoRequest;
import com.volunnear.dto.response.ActivitiesDTO;
import com.volunnear.dto.response.VolunteerProfileResponseDTO;
import com.volunnear.service.user.VolunteerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class VolunteerController {
    private final VolunteerService volunteerService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = Routes.REGISTER_VOLUNTEER, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer registrationOfVolunteer(@RequestBody RegistrationVolunteerRequest registrationVolunteerRequest) {
        return volunteerService.registerVolunteer(registrationVolunteerRequest);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping(value = Routes.UPDATE_VOLUNTEER_PROFILE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public VolunteerProfileResponseDTO updateVolunteerInfo(@RequestBody UpdateVolunteerInfoRequest updateVolunteerInfoRequest, Principal principal) {
        return volunteerService.updateVolunteerInfo(updateVolunteerInfoRequest, principal);
    }

    @GetMapping(value = Routes.GET_VOLUNTEER_PROFILE)
    @ResponseStatus(value = HttpStatus.OK)
    public VolunteerProfileResponseDTO getVolunteerProfile(Principal principal) {
        return volunteerService.getVolunteerProfile(principal);
    }

    @Operation(summary = "Delete volunteer profile", description = "Successfully or not deleted user info")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Data was successfully deleted",
                    content = @Content(schema = @Schema(implementation = ActivitiesDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad credentials, try re-login")
    })
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(value = Routes.DELETE_VOLUNTEER_PROFILE)
    public void deleteVolunteerProfile(Principal principal) {
        volunteerService.deleteVolunteerProfile(principal);
    }
}