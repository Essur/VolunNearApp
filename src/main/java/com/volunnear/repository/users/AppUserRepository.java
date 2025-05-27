package com.volunnear.repository.users;

import com.volunnear.entity.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findAppUserByUsername(String username);

    boolean existsByUsername(String username);
    void deleteAppUserByUsername(String username);
}