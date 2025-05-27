package com.volunnear.repository.infos;

import com.volunnear.entity.infos.VolunteerPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerPreferenceRepository extends JpaRepository<VolunteerPreference, Integer> {
    boolean existsByVolunteer_User_Username(String username);
    Optional<VolunteerPreference> findByVolunteer_User_Username(String username);
}