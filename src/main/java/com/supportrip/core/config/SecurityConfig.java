package com.supportrip.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportrip.core.auth.domain.Role;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                        .requestMatchers(
                                "/api/v1/auth/login",
                                "/api/v1/flight-insurances/search",
                                "/health-check",
                                "/api/v1/flight-insurances/*"
                        )
                        .permitAll()
                        .requestMatchers(
                                "/api/v1/admin/**",
                                "/api/v1/exchange/daily"
                        ).hasRole(Role.ADMIN.name())
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
                        new JwtAuthenticationFilter(jwtProvider, userRepository, objectMapper),
                        SecurityContextHolderAwareRequestFilter.class
                );

        return http.build();
    }

    @Bean
    public AesBytesEncryptor aesBytesEncryptor() {
        return new AesBytesEncryptor("${jwt.secret}", "70726574657374");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
