package com.supportrip.core.auth.controller;

import com.supportrip.core.auth.dto.LoginRequest;
import com.supportrip.core.auth.dto.LoginResponse;
import com.supportrip.core.auth.dto.RegenerateAccessTokenResponse;
import com.supportrip.core.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/api/v1/auth/login")
    public LoginResponse login(@Valid LoginRequest request) {
        return authService.login(request.getCode());
    }

    @PostMapping("/api/v1/auth/regenerate")
    public RegenerateAccessTokenResponse regenerateAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authService.regenerateAccessToken(authorization);
        return RegenerateAccessTokenResponse.from(accessToken);
    }
}