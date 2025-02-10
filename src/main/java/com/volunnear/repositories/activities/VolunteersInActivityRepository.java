package com.volunnear.repositories.activities;

import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.activities.VolunteersInActivity;
import com.volunnear.entitiy.activities.VolunteersInActivityId;
import com.volunnear.entitiy.infos.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteersInActivityRepository extends JpaRepository<VolunteersInActivity, VolunteersInActivityId> {
    List<VolunteersInActivity> findAllVolunteersInActivityByVolunteer_Username(String username);

    boolean existsByActivity_IdAndVolunteer_Username(Integer id, String username);

    void deleteByActivity_IdAndVolunteer_Username(Integer id, String username);
    void deleteByActivityAndVolunteer(Activity activity, Volunteer volunteer);
}