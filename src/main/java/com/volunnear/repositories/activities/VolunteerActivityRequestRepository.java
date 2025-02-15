package com.volunnear.repositories.activities;

import com.volunnear.ActivityRequestStatus;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteerActivityRequest;
import com.volunnear.entitiy.infos.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteerActivityRequestRepository extends JpaRepository<VolunteerActivityRequest, Integer> {
    VolunteerActivityRequest findById(int id);
    void deleteByActivityIdAndVolunteer(int activityId, Volunteer volunteer);
    Optional<VolunteerActivityRequest> findByVolunteerUsernameAndActivityId(String username, Integer activityId);
    Optional<VolunteerActivityRequest> findByActivityIdAndVolunteer_Username(Integer activityId, String username);
    List<VolunteerActivityRequest> findAllByVolunteerAndStatusLike(Volunteer volunteer, ActivityRequestStatus status);
    List<VolunteerActivityRequest> findAllByActivityInAndStatusLike(List<Activity> activities, ActivityRequestStatus status);
}