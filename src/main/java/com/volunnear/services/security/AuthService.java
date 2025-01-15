package com.volunnear.services.security;

import com.volunnear.dtos.jwt.JwtRequest;
import com.volunnear.dtos.jwt.JwtResponse;
import com.volunnear.exception.AuthErrorException;
import com.volunnear.security.jwt.JwtTokenProvider;
import com.volunnear.services.users.UserService;
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

    public JwtResponse createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthErrorException("Incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenProvider.createToken(userDetails);
        return new JwtResponse(token, userDetails.getAuthorities().toString());
    }
}