package com.supportrip.core.system.core.auth.internal.presentation;

import com.supportrip.core.system.core.auth.internal.application.AuthService;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.auth.internal.presentation.response.LoginResponse;
import com.supportrip.core.system.core.auth.internal.presentation.response.RegenerateAccessTokenResponse;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserLogService userLogService;

    @GetMapping("/api/v1/auth/login")
    public LoginResponse login(@RequestParam(name = "code") String code) {
        LoginResponse login = authService.login(code);
        Long userId = login.getUser().getId();
        userLogService.appendUserLog(userId, "User[ID=" + userId + "]  logged in successfully.");
        return login;
    }

    @PostMapping("/api/v1/auth/regenerate")
    public RegenerateAccessTokenResponse regenerateAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        String accessToken = authService.regenerateAccessToken(authorization);
        return RegenerateAccessTokenResponse.from(accessToken);
    }

    @GetMapping("/api/v1/auth/logout")
    public void logout(@AuthenticationPrincipal OidcUser oidcUser) {
        Long userId = oidcUser.getUserId();
        authService.logout(userId);
        userLogService.appendUserLog(userId, "User[ID=" + userId + "]  logged out successfully.");
    }
}