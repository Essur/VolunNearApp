package com.volunnear.repository.users;


import com.volunnear.entity.users.AppUser;
import com.volunnear.entity.users.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByAppUser(AppUser user);
    void deleteByAppUser_Username(String username);
    boolean existsByAppUser(AppUser appUser);
    void deleteByToken(String token);
}
