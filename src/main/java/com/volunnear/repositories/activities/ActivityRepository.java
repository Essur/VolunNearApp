package com.volunnear.repositories.activities;

import com.volunnear.entitiy.activities.Activities;
import com.volunnear.entitiy.activities.Activity;
import com.volunnear.entitiy.infos.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    List<Activity> findAllActivitiesByKindOfActivity_NameIgnoreCaseIn(List<String> preferences);
}