package com.volunnear.service.activity;

import com.volunnear.dto.request.activity.ActivitySaveRequestDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.entity.activity.Activity;
import com.volunnear.entity.profile.OrganizationProfile;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.mapper.activity.ActivityMapper;
import com.volunnear.repository.activity.ActivityRepository;
import com.volunnear.service.profile.OrganizationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityMapper activityMapper;
    private final GeometryFactory geometryFactory;
    private final ActivityRepository activityRepository;
    private final OrganizationService organizationService;

    public Long createActivity(ActivitySaveRequestDTO requestDTO, Principal principal) {
        OrganizationProfile organizationProfile = organizationService.findOrganizationProfileByPrincipal(principal);
        Activity activity = activityMapper.toEntity(requestDTO, organizationProfile, geometryFactory);
        activity.setOrganizationProfile(organizationProfile);
        activityRepository.save(activity);
        return activity.getId();
    }

    public ActivityResponseDTO updateActivity(@Valid ActivitySaveRequestDTO requestDTO, @NotBlank Long id, Principal principal) {
        Activity activity = activityRepository.findByIdAndOrganizationProfile_AppUser_Username(id, principal.getName())
                .orElseThrow(() -> new DataNotFoundException("Activity with id: " + id + " not found"));
        activityMapper.updateEntity(requestDTO, activity, geometryFactory);
        activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }
}
