package com.volunnear.util;

import com.volunnear.entity.users.AppUser;
import com.volunnear.exception.BadUserCredentialsException;
import com.volunnear.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Principal;


@Component
@RequiredArgsConstructor
public class PrincipalUtils {
    private final UserService userService;

    public AppUser getUserFromPrincipal(Principal principal) {
        return userService.findAppUserByUsername(principal.getName())
                .orElseThrow(() -> new BadUserCredentialsException("User with username" + principal.getName() + " not found"));
    }
}
