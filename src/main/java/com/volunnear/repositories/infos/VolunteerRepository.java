package com.volunnear.repositories.infos;

import com.volunnear.entitiy.infos.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
    Optional<Volunteer> findByUsername(String username);

    boolean existsByUsername(String username);
}