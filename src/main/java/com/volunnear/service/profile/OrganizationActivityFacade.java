package com.volunnear.service.profile;

import com.volunnear.dto.request.activity.ActivitySaveRequestDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.entity.profile.OrganizationProfile;
import com.volunnear.service.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationActivityFacade {
    private final ActivityService activityService;
    private final OrganizationService organizationService;

    public ActivityResponseDTO createActivity(ActivitySaveRequestDTO requestDTO, Principal principal) {
        OrganizationProfile organizationProfile = organizationService.getOrganizationProfileByPrincipal(principal);
        return activityService.createActivity(requestDTO, organizationProfile);
    }
}