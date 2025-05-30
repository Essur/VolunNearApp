package com.volunnear.repository.profile;

import com.volunnear.entity.profile.VolunteerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfile, Long> {
}