package com.volunnear.repositories.activities;

import com.volunnear.entity.activities.Activity;
import com.volunnear.entity.activities.VolunteersInActivity;
import com.volunnear.entity.activities.VolunteersInActivityId;
import com.volunnear.entity.infos.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteersInActivityRepository extends JpaRepository<VolunteersInActivity, VolunteersInActivityId> {
    List<VolunteersInActivity> findAllVolunteersInActivityByVolunteer_User_Username(String username);
    List<VolunteersInActivity> findAllByActivity_Id(Integer activityId);

    boolean existsByActivity_IdAndVolunteer_User_Username(Integer id, String username);

    void deleteByActivity_IdAndVolunteer_User_Username(Integer id, String username);
    void deleteByActivityAndVolunteer(Activity activity, Volunteer volunteer);
}