package com.volunnear.services;

import com.volunnear.dtos.requests.AddPreferenceRequest;
import com.volunnear.entity.infos.VolunteerPreference;
import com.volunnear.exception.DataNotFoundException;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.repositories.infos.VolunteerPreferenceRepository;
import com.volunnear.services.users.VolunteerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class VolunteerRecommendationService {
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
    public VolunteerPreference updateVolunteerPreferences (AddPreferenceRequest preferenceRequest, Principal principal) {
        if (!volunteerService.isVolunteerExist(principal.getName())){
            throw new DataNotFoundException("Volunteer does not exist");
        }
        VolunteerPreference volunteerPreference = volunteerPreferenceRepository.findByVolunteer_User_Username(principal.getName()).get();
        volunteerPreference.setLocation(geometryFactory.createPoint(new Coordinate(preferenceRequest.getLatitude(), preferenceRequest.getLongitude())));
        volunteerPreferenceRepository.save(volunteerPreference);
        log.info("Volunteer preference updated: {}", volunteerPreference);
        return volunteerPreference;
    }
}
