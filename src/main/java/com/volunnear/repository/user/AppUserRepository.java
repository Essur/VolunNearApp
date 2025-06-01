package com.volunnear.repository.user;

import com.volunnear.entity.users.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findAppUserByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    void deleteByUsername(String username);
}