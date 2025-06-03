package com.volunnear.repository.profile;

import com.volunnear.entity.profile.OrganizationProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationProfileRepository extends JpaRepository<OrganizationProfile, Long> {
    Optional<OrganizationProfile> findByAppUser_Username(String username);
    boolean existsByAppUser_Username(String username);
}