package com.supportrip.core.system.core.user.internal.presentation.response;

import com.supportrip.core.system.core.user.internal.domain.Gender;
import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AdminUserDetailResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate birthDay;
    private Gender gender;
    private LocalDateTime joinedAt;
    private String profileImageUrl;
    private boolean enabled;
    private boolean notificationStatus;

    @Builder(access = AccessLevel.PRIVATE)
    private AdminUserDetailResponse(Long id, String name, String email, String phoneNumber, LocalDate birthDay, Gender gender, LocalDateTime joinedAt, String profileImageUrl, boolean enabled, boolean notificationStatus) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDay = birthDay;
        this.gender = gender;
        this.joinedAt = joinedAt;
        this.profileImageUrl = profileImageUrl;
        this.enabled = enabled;
        this.notificationStatus = notificationStatus;
    }

    public static AdminUserDetailResponse of(User user, boolean status) {
        return AdminUserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDay(user.getBirthDay())
                .gender(user.getGender())
                .joinedAt(user.getCreatedAt())
                .profileImageUrl(user.getProfileImageUrl())
                .enabled(user.isEnabled())
                .notificationStatus(status)
                .build();
    }
}
