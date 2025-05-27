package com.volunnear.service.security;

import com.volunnear.dto.jwt.JwtRequest;
import com.volunnear.exception.AuthErrorException;
import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthErrorException("Incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        return jwtTokenProvider.createToken(userDetails);
    }

    public String recreateToken(String username){
        UserDetails userDetails = userService.loadUserByUsername(username);
        return jwtTokenProvider.createToken(userDetails);
    }

    public String getAuthorities(String token) {
        return jwtTokenProvider.getRolesFromToken(token).getFirst();
    }

    public String getUsernameByToken(String token) {
        return jwtTokenProvider.getUsernameFromToken(token);
    }
}