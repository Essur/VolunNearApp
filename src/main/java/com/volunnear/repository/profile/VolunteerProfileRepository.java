package com.volunnear.repository.profile;

import com.volunnear.entity.profile.VolunteerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfile, Long> {
    boolean existsByAppUser_Username(String username);
    Optional<VolunteerProfile> findByAppUser_Username(String username);
}