package com.volunnear.repository.activity;

import com.volunnear.entity.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Optional<Activity> findByIdAndOrganizationProfile_AppUser_Username(Long id, String username);
}