package com.volunnear.repositories.activities;

import com.volunnear.entitiy.activities.Activities;
import com.volunnear.entitiy.activities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ActivitiesRepository extends JpaRepository<Activities, Integer> {
    Optional<Activities> findActivitiesByActivityId(Integer id);
    Optional<Activities> findActivitiesByActivity_Id(Integer activityId);
    List<Activities> findAllByOrganization_Username(String username);

    List<Activities> findAllByOrganization_Name(String name);
    List<Activities> findAllByOrganization_Id(Integer id);

    List<Activities> findAllByActivityIn(List<Activity> activities);

    List<Activities> findAllByActivity_CountryAndActivity_City(String country, String city);

    Optional<Activities> findByOrganization_UsernameAndActivity_Id(String username, Integer id);
    void deleteAllByOrganization_Id(Integer organizationId);
}