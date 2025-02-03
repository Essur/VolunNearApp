package com.volunnear.controllers;

import com.volunnear.Routes;
import com.volunnear.dtos.jwt.JwtRefreshRequest;
import com.volunnear.dtos.jwt.JwtRefreshTokenResponse;
import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.jwt.JwtResponse;
import com.volunnear.entitiy.users.RefreshToken;
import com.volunnear.exception.TokenRefreshException;
import com.volunnear.services.security.AuthService;
import com.volunnear.services.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.LOGIN)
    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
        String token = authService.createAuthToken(authRequest);
        String role = authService.getAuthorities(token);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authService.getUsernameByToken(token));
        return new JwtResponse(token, role, refreshToken.getToken());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = Routes.REFRESH_TOKEN)
    public JwtRefreshTokenResponse refreshAuthToken(@RequestBody JwtRefreshRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getAppUser)
                .map(user -> {
                    String token = authService.recreateToken(user.getUsername());
                    return new JwtRefreshTokenResponse(token, refreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException("Bad credentials, try re-login"));
    }
}
