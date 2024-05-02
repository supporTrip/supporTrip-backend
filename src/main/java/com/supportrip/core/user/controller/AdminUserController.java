package com.supportrip.core.user.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.log.domain.UserLog;
import com.supportrip.core.log.dto.UserLogListResponse;
import com.supportrip.core.log.service.UserLogService;
import com.supportrip.core.user.dto.admin.AdminUserDetailResponse;
import com.supportrip.core.user.dto.admin.AdminUserEnabledUpdateRequest;
import com.supportrip.core.user.dto.admin.AdminUserEnabledUpdatedResponse;
import com.supportrip.core.user.dto.admin.AdminUserResponse;
import com.supportrip.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;
    private final UserLogService userLogService;

    @GetMapping("/api/v1/admin/users")
    public List<AdminUserResponse> adminGetUsersInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        return userService.getUsers(oidcUser.getUserId());
    }

    @GetMapping("/api/v1/admin/users/{id}")
    public AdminUserDetailResponse adminGetUserInfo(@AuthenticationPrincipal OidcUser oidcUser,
                                                    @PathVariable("id") Long id) {
        return userService.getUserInfo(oidcUser.getUserId(), id);
    }

    @PutMapping("/api/v1/admin/users")
    public AdminUserEnabledUpdatedResponse adminUserUpdate(@AuthenticationPrincipal OidcUser oidcUser,
                                                           @RequestBody AdminUserEnabledUpdateRequest request) {
        return userService.userEnabledUpdate(oidcUser.getUserId(), request);
    }

    @GetMapping("/api/v1/admin/users/{userId}/logs")
    public UserLogListResponse getUserLog(@PathVariable Long userId) {
        List<UserLog> userLogs = userLogService.getUserLogs(userId);
        return UserLogListResponse.from(userLogs);
    }
}
