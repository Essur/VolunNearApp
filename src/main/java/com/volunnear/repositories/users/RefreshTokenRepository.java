package com.volunnear.repositories.users;


import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByAppUser(AppUser user);
    void deleteByAppUser_Username(String username);
    boolean existsByAppUser(AppUser appUser);
    void deleteByToken(String token);
}
