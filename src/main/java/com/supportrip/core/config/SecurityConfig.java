package com.supportrip.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportrip.core.auth.filter.Http401AuthenticationEntryPoint;
import com.supportrip.core.auth.filter.Http403AccessDeniedHandler;
import com.supportrip.core.auth.filter.JwtAuthenticationFilter;
import com.supportrip.core.auth.jwt.JwtProvider;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/login", "/health-check")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(new Http403AccessDeniedHandler(objectMapper))
                        .authenticationEntryPoint(new Http401AuthenticationEntryPoint(objectMapper))
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider, userRepository),
                        SecurityContextHolderAwareRequestFilter.class
                );

        return http.build();
    }
}
