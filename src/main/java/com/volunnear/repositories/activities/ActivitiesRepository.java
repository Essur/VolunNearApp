package com.volunnear.repositories.activities;

import com.volunnear.entitiy.activities.Activities;
import com.volunnear.entitiy.activities.ActivitiesId;
import com.volunnear.entitiy.activities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ActivitiesRepository extends JpaRepository<Activities, ActivitiesId> {
    Optional<Activities> findActivitiesByActivityId(Integer id);

    List<Activities> findAllByOrganisation_Username(String username);

    List<Activities> findAllByOrganisation_Name(String name);

    List<Activities> findAllByActivityIn(List<Activity> activities);

    List<Activities> findAllByActivity_CountryAndActivity_City(String country, String city);

    Optional<Activities> findByOrganisation_UsernameAndActivity_Id(String username, Integer id);
}