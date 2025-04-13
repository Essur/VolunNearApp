package com.volunnear.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRefreshTokenResponse {
    private String token;
    private String refreshToken;
}
