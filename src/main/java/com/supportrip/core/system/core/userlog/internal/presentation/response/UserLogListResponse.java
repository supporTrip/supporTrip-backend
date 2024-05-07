package com.supportrip.core.system.core.userlog.internal.presentation.response;

import com.supportrip.core.system.core.userlog.internal.domain.UserLog;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserLogListResponse {
    private final List<UserLogResponse> userLogs;

    @Builder(access = AccessLevel.PRIVATE)
    private UserLogListResponse(List<UserLogResponse> userLogs) {
        this.userLogs = userLogs;
    }

    public static UserLogListResponse from(List<UserLog> userLogs) {
        List<UserLogResponse> userLogResponses = userLogs.stream()
                .map(UserLogResponse::from)
                .toList();

        return UserLogListResponse.builder()
                .userLogs(userLogResponses)
                .build();
    }

    @Getter
    static class UserLogResponse {
        private final Long userId;
        private final String message;
        private final LocalDateTime createdAt;

        @Builder(access = AccessLevel.PRIVATE)
        public UserLogResponse(Long userId, String message, LocalDateTime createdAt) {
            this.userId = userId;
            this.message = message;
            this.createdAt = createdAt;
        }

        public static UserLogResponse from(UserLog userLog) {
            return UserLogResponse.builder()
                    .userId(userLog.getUser().getId())
                    .message(userLog.getMessage())
                    .createdAt(userLog.getCreatedAt())
                    .build();
        }
    }
}
