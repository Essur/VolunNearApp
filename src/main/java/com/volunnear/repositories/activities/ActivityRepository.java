package com.volunnear.repositories.activities;

import com.volunnear.entitiy.activities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    List<Activity> findAllActivitiesByKindOfActivityIgnoreCaseIn(List<String> preferences);

    boolean deleteAllById(Integer id);
}