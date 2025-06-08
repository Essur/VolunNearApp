package com.volunnear.controller;

import com.volunnear.Routes;
import com.volunnear.dto.request.activity.ActivitySaveRequestDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.service.activity.ActivityService;
import com.volunnear.service.profile.OrganizationActivityFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    private final OrganizationActivityFacade organizationActivityFacade;

    @GetMapping(value = Routes.ACTIVITY_BY_ID)
    public ActivityResponseDTO getActivityById(@PathVariable("id") Long id) {
        return activityService.getActivityById(id);
    }

    @GetMapping(value = Routes.ACTIVITIES)
    public List<ActivityResponseDTO> getAllActivities() {
        return activityService.getAllActivities();
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PostMapping(value = Routes.ACTIVITIES)
    public ActivityResponseDTO createActivity(@Valid @RequestBody ActivitySaveRequestDTO requestDTO, Principal principal) {
        return organizationActivityFacade.createActivity(requestDTO, principal);
    }

    @PreAuthorize("hasRole('ORGANIZATION')")
    @PutMapping(value = Routes.ACTIVITY_BY_ID)
    public ActivityResponseDTO updateActivity(@Valid @RequestBody ActivitySaveRequestDTO requestDTO,
                                              @NotNull @PathVariable("id") Long id,
                                              Principal principal) {
        return activityService.updateActivity(requestDTO, id, principal);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ORGANIZATION')")
    @DeleteMapping(value = Routes.ACTIVITY_BY_ID)
    public void deleteActivityById(@PathVariable("id") Long id, Principal principal) {
        activityService.deleteActivityById(id, principal);
    }
}
