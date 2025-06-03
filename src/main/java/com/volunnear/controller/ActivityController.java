package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.request.activity.ActivitySaveRequestDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.service.activity.ActivityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PostMapping(value = Routes.ACTIVITIES)
    public Long createActivity(@Valid @RequestBody ActivitySaveRequestDTO requestDTO, Principal principal) {
        return activityService.createActivity(requestDTO, principal);
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PutMapping(value = Routes.ACTIVITY_BY_ID)
    public ActivityResponseDTO updateActivity(@Valid @RequestBody ActivitySaveRequestDTO requestDTO,
                                              @NotNull @PathVariable("id") Long id,
                                              Principal principal) {
        return activityService.updateActivity(requestDTO, id, principal);
    }
}
