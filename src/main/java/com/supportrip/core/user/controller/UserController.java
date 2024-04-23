package com.supportrip.core.user.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.insurance.dto.UserInfoResponse;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.dto.SignUpRequest;
import com.supportrip.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/api/v1/users/signup")
    public SimpleIdResponse signUp(@Valid @RequestBody SignUpRequest request,
                                   @AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.signUp(oidcUser.getUserId(), request);
        return SimpleIdResponse.from(user.getId());
    }

    @GetMapping("/api/v1/flight-insurance/users")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return ResponseEntity.ok(UserInfoResponse.of(user.getName(), user.getGender() , user.getBirthDay()));
    }
}
