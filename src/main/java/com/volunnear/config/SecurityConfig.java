package com.volunnear.config;

import com.volunnear.Routes;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(Routes.REGISTER_ROUTE_SECURITY + "/**",
                                Routes.LOGIN).permitAll()

                        .requestMatchers(Routes.SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers("/v3/api-docs", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()

                        .requestMatchers(Routes.VOLUNTEER + "/**",
                                Routes.UPDATE_VOLUNTEER_PROFILE,
                                Routes.POST_FEEDBACK_ABOUT_ORGANIZATION,
                                Routes.UPDATE_FEEDBACK_FOR_CURRENT_ORGANIZATION,
                                Routes.DELETE_FEEDBACK_ABOUT_ORGANIZATION,
                                Routes.LOCATION + "/**",
                                Routes.NOTIFICATIONS + "/**").hasRole("VOLUNTEER")

                        .requestMatchers(Routes.UPDATE_ORGANIZATION_PROFILE,
                                Routes.ADD_ACTIVITY,
                                Routes.GET_MY_ACTIVITIES,
                                Routes.UPDATE_ACTIVITY_INFORMATION,
                                Routes.DELETE_CURRENT_ACTIVITY_BY_ID,
                                Routes.GET_ORGANIZATION_PROFILE,
                                Routes.ADD_COMMUNITY_LINK,
                                Routes.ADD_CHAT_LINK_FOR_ACTIVITY).hasRole("ORGANIZATION")

                        .requestMatchers("/api/hello",
                                Routes.GET_ALL_ORGANIZATIONS,
                                Routes.ACTIVITY_CURRENT_ORGANIZATION,
                                Routes.GET_ALL_ACTIVITIES_WITH_ALL_ORGANIZATIONS,
                                Routes.GET_FEEDBACKS_OF_ALL_ORGANIZATIONS,
                                Routes.GET_FEEDBACKS_FROM_CURRENT_ORGANIZATION,
                                Routes.GET_CHAT_LINK_BY_ACTIVITY,
                                Routes.GET_COMMUNITY_LINK_BY_ORGANIZATION)
                        .hasAnyRole("VOLUNTEER", "ORGANIZATION")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
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
