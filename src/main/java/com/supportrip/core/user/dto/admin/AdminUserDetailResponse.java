package com.supportrip.core.user.dto.admin;

import com.supportrip.core.user.domain.Gender;
import com.supportrip.core.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AdminUserDetailResponse {
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
    private AdminUserDetailResponse(String name, String email, String phoneNumber, LocalDate birthDay, Gender gender, LocalDateTime joinedAt, String profileImageUrl, boolean enabled, boolean notificationStatus) {
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
