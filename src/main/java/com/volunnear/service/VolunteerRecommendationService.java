package com.volunnear.service;

import com.volunnear.dto.VolunteerPreferenceDTO;
import com.volunnear.dto.requests.AddPreferenceRequest;
import com.volunnear.dto.response.ActivitiesDTO;
import com.volunnear.entity.infos.VolunteerPreference;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.repository.infos.VolunteerPreferenceRepository;
import com.volunnear.service.activity.ActivityService;
import com.volunnear.service.user.VolunteerService;
import com.volunnear.util.ActivityWithDistance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VolunteerRecommendationService {
    private final ActivityService activityService;
    private final VolunteerService volunteerService;
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final VolunteerPreferenceRepository volunteerPreferenceRepository;

    @Transactional
    public Integer addVolunteerPreferences (AddPreferenceRequest preferenceRequest, Principal principal) {
        if (!volunteerService.isVolunteerExist(principal.getName())){
            throw new DataNotFoundException("Volunteer does not exist");
        } else if(volunteerPreferenceRepository.existsByVolunteer_User_Username(principal.getName())){
            throw new UserAlreadyExistsException("Preference already set, try update method");
        }
        VolunteerPreference volunteerPreference = new VolunteerPreference();
        volunteerPreference.setLocation(geometryFactory.createPoint(new Coordinate(
                preferenceRequest.getLatitude(),
                preferenceRequest.getLongitude())));
        volunteerPreference.setVolunteer(volunteerService.getVolunteerInfo(principal).get());
        volunteerPreferenceRepository.save(volunteerPreference);

        log.info("Volunteer preference added: {}", volunteerPreference);
        log.info("Lon: {} Lat: {}", preferenceRequest.getLongitude(), preferenceRequest.getLatitude());

        return volunteerPreference.getId();
    }

    @Transactional
    public VolunteerPreferenceDTO updateVolunteerPreferences (AddPreferenceRequest preferenceRequest, Principal principal) {
        if (!volunteerService.isVolunteerExist(principal.getName())){
            throw new DataNotFoundException("Volunteer does not exist");
        }
        VolunteerPreference volunteerPreference = volunteerPreferenceRepository.findByVolunteer_User_Username(principal.getName()).get();
        volunteerPreference.setLocation(geometryFactory.createPoint(new Coordinate(preferenceRequest.getLatitude(), preferenceRequest.getLongitude())));
        volunteerPreferenceRepository.save(volunteerPreference);
        log.info("Volunteer preference updated: {}", volunteerPreference);
        return new VolunteerPreferenceDTO(
                volunteerPreference.getId(),
                volunteerPreference.getVolunteer().getUser().getUsername(),
                volunteerPreference.getLocation().getX(),
                volunteerPreference.getLocation().getY());
    }

    public List<ActivitiesDTO> getRecommendationsForUser (Integer range, Principal principal) {
        if (!volunteerService.isVolunteerExist(principal.getName())){
            throw new DataNotFoundException("Volunteer does not exist");
        }
        VolunteerPreference volunteerPreference = volunteerPreferenceRepository.findByVolunteer_User_Username(principal.getName()).get();
        List<ActivityWithDistance> activitiesInRadiusByLocation =
                activityService.getActivitiesInRadiusByLocation(range, volunteerPreference.getLocation());

        log.info("Activities in radius by user location: {}", activitiesInRadiusByLocation);
        log.info("Range: {} ", range);

        if (activitiesInRadiusByLocation.isEmpty()) {
            throw new DataNotFoundException("No activities in your range found");
        }
        List<ActivitiesDTO> infoAboutOrganizations = activityService.getInfoAboutOrganizations(activitiesInRadiusByLocation);

        log.info("Activities in radius by organization: {}", infoAboutOrganizations);
        return infoAboutOrganizations;
    }
}
