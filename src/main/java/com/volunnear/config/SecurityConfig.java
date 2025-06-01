package com.volunnear.config;

import com.volunnear.Routes;
import com.volunnear.security.jwt.JwtAuthEntryPoint;
import com.volunnear.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(Routes.REGISTER + "/**",
                                Routes.LOGIN,
                                Routes.GET_ACTIVITY_INFO,
                                Routes.GET_ALL_ACTIVITIES,
                                Routes.GET_ALL_ORGANIZATIONS,
                                Routes.ACTIVITY_CURRENT_ORGANIZATION,
                                Routes.GET_FEEDBACKS_OF_ALL_ORGANIZATIONS,
                                Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANIZATION,
                                Routes.GET_CHAT_LINK_BY_ACTIVITY,
                                Routes.GET_COMMUNITY_LINK_BY_ORGANIZATION).permitAll()
                        .requestMatchers(Routes.SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers("/v3/api-docs", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
                        .requestMatchers(Routes.REFRESH_TOKEN).hasAnyRole("VOLUNTEER", "ORGANIZATION")

                        .requestMatchers(Routes.VOLUNTEER + "/**",
                                Routes.UPDATE_VOLUNTEER_PROFILE,
                                Routes.POST_FEEDBACK_ABOUT_ORGANIZATION,
                                Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANIZATION,
                                Routes.DELETE_FEEDBACK_ABOUT_ORGANIZATION,
                                Routes.LOCATION + "/**",
                                Routes.NOTIFICATIONS + "/**",
                                Routes.JOIN_TO_ACTIVITY_REQUEST,
                                Routes.DELETE_MY_JOIN_ACTIVITY_REQUEST,
                                Routes.LEAVE_FROM_ACTIVITY_BY_VOLUNTEER,
                                Routes.GET_VOLUNTEER_ACTIVITY_REQUEST_STATUS_INFO,
                                Routes.GET_VOLUNTEER_ACTIVITY_REQUESTS,
                                Routes.ADD_PREFERENCE_TO_VOLUNTEER,
                                Routes.UPDATE_PREFERENCE_TO_VOLUNTEER,
                                Routes.GET_RECOMMENDATION_BY_PREFERENCES).hasRole("VOLUNTEER")

                        .requestMatchers(Routes.UPDATE_ORGANIZATION_PROFILE,
                                Routes.ADD_ACTIVITY,
                                Routes.GET_MY_ACTIVITIES,
                                Routes.UPDATE_ACTIVITY_INFORMATION,
                                Routes.DELETE_CURRENT_ACTIVITY_BY_ID,
                                Routes.GET_ORGANIZATION_PROFILE,
                                Routes.ADD_COMMUNITY_LINK,
                                Routes.ADD_CHAT_LINK_FOR_ACTIVITY,
                                Routes.APPROVE_VOLUNTEER_TO_ACTIVITY,
                                Routes.KICK_VOLUNTEER_FORM_ACTIVITY,
                                Routes.GET_ORGANIZATION_ACTIVITY_REQUESTS,
                                Routes.GET_ORGANIZATION_ID,
                                Routes.GET_VOLUNTEERS_FROM_CURRENT_ACTIVITY).hasRole("ORGANIZATION")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .logout(Customizer.withDefaults());
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SneakyThrows
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
