package com.volunnear.service.activity;

import com.volunnear.dto.request.activity.ActivitySaveRequestDTO;
import com.volunnear.dto.response.activity.ActivityResponseDTO;
import com.volunnear.entity.activity.Activity;
import com.volunnear.entity.profile.OrganizationProfile;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.mapper.activity.ActivityMapper;
import com.volunnear.repository.activity.ActivityRepository;
import com.volunnear.service.GeoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final GeoService geoService;
    private final ActivityMapper activityMapper;
    private final GeometryFactory geometryFactory;
    private final ActivityRepository activityRepository;

    public ActivityResponseDTO createActivity(ActivitySaveRequestDTO requestDTO, OrganizationProfile organizationProfile) {
        Point point = resolvePointFromRequest(requestDTO);
        Activity activity = activityMapper.toEntity(requestDTO, organizationProfile, point, geometryFactory);
        activity.setOrganizationProfile(organizationProfile);
        activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    public ActivityResponseDTO updateActivity(ActivitySaveRequestDTO requestDTO, Long id, Principal principal) {
        Point point = resolvePointFromRequest(requestDTO);
        Activity activity = activityRepository.findByIdAndOrganizationProfile_AppUser_Username(id, principal.getName())
                .orElseThrow(() -> new DataNotFoundException("Activity with id: " + id + " not found"));
        activityMapper.updateEntity(requestDTO, point, activity, geometryFactory);
        activityRepository.save(activity);
        return activityMapper.toDto(activity);
    }

    public ActivityResponseDTO getActivityById(Long id) {
        Activity activity = activityRepository.findActivityById(id)
                .orElseThrow(() -> new DataNotFoundException("Activity with id: " + id + " not found"));
        return activityMapper.toDto(activity);
    }

    public List<ActivityResponseDTO> getActivitiesByOrganizationId(Long organizationId) {
        List<Activity> activitiesByOrganizationId = activityRepository.findAllByOrganizationProfile_Id(organizationId);
        if (activitiesByOrganizationId.isEmpty()) {
            throw new DataNotFoundException("Activities by organization id " + organizationId + " not found");
        }
        return activitiesByOrganizationId.stream().map(activityMapper::toDto).toList();
    }

    public List<ActivityResponseDTO> getActivitiesByPrincipal(Principal principal) {
        List<Activity> allByPrincipal = activityRepository.findAllByOrganizationProfile_AppUser_Username(principal.getName());
        if (allByPrincipal.isEmpty()) {
            throw new DataNotFoundException("Activities by principal not found");
        }
        return allByPrincipal.stream().map(activityMapper::toDto).toList();
    }

    public List<ActivityResponseDTO> getAllActivities() {
        List<Activity> allActivities = activityRepository.findAll();
        if (allActivities.isEmpty()) {
            throw new DataNotFoundException("No activities in our database");
        }
        return allActivities.stream().map(activityMapper::toDto).toList();
    }

    public void deleteActivityById(Long id, Principal principal) {
        if (activityRepository.deleteByIdAndOrganizationProfile_AppUser_Username(id, principal.getName()) == 0) {
            throw new DataNotFoundException("Activity not found or not owned by user");
        }
    }

    private Point resolvePointFromRequest(ActivitySaveRequestDTO dto) {
        return geoService.geocode(dto.getLocation());
    }
}
