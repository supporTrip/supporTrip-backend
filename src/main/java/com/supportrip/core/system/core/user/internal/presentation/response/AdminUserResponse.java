package com.supportrip.core.system.core.user.internal.presentation.response;

import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AdminUserResponse {
    private Long id;
    private LocalDateTime joinedAt;
    private String name;
    private LocalDate birthDay;
    private boolean enabled;

    @Builder(access = AccessLevel.PRIVATE)
    private AdminUserResponse(Long id, LocalDateTime joinedAt, String name, LocalDate birthDay, boolean enabled) {
        this.id = id;
        this.joinedAt = joinedAt;
        this.name = name;
        this.birthDay = birthDay;
        this.enabled = enabled;
    }

    public static AdminUserResponse of (User user) {
        return AdminUserResponse.builder()
                .id(user.getId())
                .joinedAt(user.getCreatedAt())
                .name(user.getName())
                .birthDay(user.getBirthDay())
                .enabled(user.isEnabled())
                .build();
    }
}
