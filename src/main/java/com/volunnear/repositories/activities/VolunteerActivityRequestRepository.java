package com.volunnear.repositories.activities;

import com.volunnear.entitiy.activities.VolunteerActivityRequest;
import com.volunnear.entitiy.infos.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerActivityRequestRepository extends JpaRepository<VolunteerActivityRequest, Integer> {
    VolunteerActivityRequest findById(int id);
    void deleteByActivityIdAndVolunteer(int activityId, Volunteer volunteer);
    Optional<VolunteerActivityRequest> findByActivityIdAndVolunteer_Username(Integer activityId, String username);
}