package com.supportrip.core.system.core.user.internal.presentation;

import com.supportrip.core.system.core.user.internal.application.UserService;
import com.supportrip.core.system.core.user.internal.presentation.request.AdminUserEnabledUpdateRequest;
import com.supportrip.core.system.core.user.internal.presentation.response.AdminUserDetailResponse;
import com.supportrip.core.system.core.user.internal.presentation.response.AdminUserEnabledUpdatedResponse;
import com.supportrip.core.system.core.user.internal.presentation.response.AdminUserResponse;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
import com.supportrip.core.system.core.userlog.internal.domain.UserLog;
import com.supportrip.core.system.core.userlog.internal.presentation.response.UserLogListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;
    private final UserLogService userLogService;

    @GetMapping("/api/v1/admin/users")
    public List<AdminUserResponse> adminGetUsersInfo() {
        return userService.getUsers();
    }

    @GetMapping("/api/v1/admin/users/{id}")
    public AdminUserDetailResponse adminGetUserInfo(@PathVariable("id") Long id) {
        return userService.getUserInfo(id);
    }

    @PutMapping("/api/v1/admin/users")
    public AdminUserEnabledUpdatedResponse adminUserUpdate(@RequestBody AdminUserEnabledUpdateRequest request) {
        return userService.userEnabledUpdate(request);
    }

    @GetMapping("/api/v1/admin/users/{userId}/logs")
    public UserLogListResponse getUserLog(@PathVariable Long userId) {
        List<UserLog> userLogs = userLogService.getUserLogs(userId);
        return UserLogListResponse.from(userLogs);
    }
}
