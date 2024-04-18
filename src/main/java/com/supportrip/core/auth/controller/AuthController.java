package com.supportrip.core.auth.controller;

import com.supportrip.core.auth.dto.LoginRequest;
import com.supportrip.core.auth.dto.LoginResponse;
import com.supportrip.core.auth.dto.RegenerateAccessTokenResponse;
import com.supportrip.core.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/api/v1/auth/login")
    public LoginResponse login(@RequestParam(required = true, name = "code") String code) {
        return authService.login(code);
    }

    @PostMapping("/api/v1/auth/regenerate")
    public RegenerateAccessTokenResponse regenerateAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authService.regenerateAccessToken(authorization);
        return RegenerateAccessTokenResponse.from(accessToken);
    }
}