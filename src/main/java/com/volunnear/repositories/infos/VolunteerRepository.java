package com.volunnear.repositories.infos;

import com.volunnear.entity.infos.Volunteer;
import com.volunnear.entity.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Integer> {
    Optional<Volunteer> findByUser_Username(String username);

    boolean existsByUser_Username(String username);

    String user(AppUser user);
}