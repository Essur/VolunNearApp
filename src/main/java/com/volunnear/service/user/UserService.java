package com.volunnear.service.user;

import com.volunnear.UserRole;
import com.volunnear.dto.request.user.RegisterAppUserDTO;
import com.volunnear.entity.users.AppUser;
import com.volunnear.exception.UserAlreadyExistsException;
import com.volunnear.mapper.user.AppUserMapper;
import com.volunnear.repository.user.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findAppUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                String.format("User with " + username + " not found")
        ));
        return User.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .roles(appUser.getRoles().toArray(new String[0]))
                .build();
    }

    public Optional<AppUser> findAppUserByUsername(String username) {
        return appUserRepository.findAppUserByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return appUserRepository.existsByUsername(username);
    }

    public Long registerAppUser(RegisterAppUserDTO requestDto, UserRole role) {
        if (appUserRepository.existsByUsernameOrEmail(requestDto.getUsername(), requestDto.getEmail())) {
            throw new UserAlreadyExistsException("User with username " + requestDto.getUsername() + " already exists");
        }
        log.info("Register AppUser request: {}", requestDto);
        AppUser user = AppUserMapper.mapper
                .toEntity(requestDto, role == UserRole.VOLUNTEER ? "VOLUNTEER" : "ORGANIZATION");

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        log.info("Register AppUser response: {}", user);
        appUserRepository.save(user);
        return user.getId();
    }
}
