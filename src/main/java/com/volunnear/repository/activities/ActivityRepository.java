package com.volunnear.repository.activities;

import com.volunnear.entity.activities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    List<Activity> findAllActivitiesByKindOfActivityIgnoreCaseIn(List<String> preferences);

    boolean deleteAllById(Integer id);
}