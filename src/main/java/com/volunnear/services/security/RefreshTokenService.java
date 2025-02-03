package com.volunnear.services.security;

import com.volunnear.entitiy.users.AppUser;
import com.volunnear.entitiy.users.RefreshToken;
import com.volunnear.exception.TokenRefreshException;
import com.volunnear.repositories.users.AppUserRepository;
import com.volunnear.repositories.users.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refreshToken.lifetime}")
    private Long expireDate;

    private final AppUserRepository appUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username) {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).get();
        if (!refreshTokenRepository.existsByAppUser(appUser)) {
            RefreshToken refreshToken = new RefreshToken();

            refreshToken.setAppUser(appUser);
            refreshToken.setExpiryDate(Instant.now().plusMillis(expireDate));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        return refreshTokenRepository.findByAppUser(appUser).get();
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new sign-in request");
        }
        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
