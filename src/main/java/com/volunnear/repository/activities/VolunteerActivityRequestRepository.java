package com.volunnear.repository.activities;

import com.volunnear.ActivityRequestStatus;
import com.volunnear.entity.activities.Activity;
import com.volunnear.entity.activities.VolunteerActivityRequest;
import com.volunnear.entity.infos.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerActivityRequestRepository extends JpaRepository<VolunteerActivityRequest, Integer> {
    void deleteByActivityIdAndVolunteer(int activityId, Volunteer volunteer);
    Optional<VolunteerActivityRequest> findByVolunteer_User_UsernameAndActivityId(String username, Integer activityId);
    Optional<VolunteerActivityRequest> findByActivityIdAndVolunteer_User_Username(Integer activityId, String username);
    List<VolunteerActivityRequest> findAllByVolunteerAndStatusLike(Volunteer volunteer, ActivityRequestStatus status);
    List<VolunteerActivityRequest> findAllByActivityInAndStatusLike(List<Activity> activities, ActivityRequestStatus status);
}