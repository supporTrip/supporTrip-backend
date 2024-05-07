package com.supportrip.core.system.core.insurance.internal.presentation.response;

import com.supportrip.core.system.core.auth.internal.domain.Role;
import com.supportrip.core.system.core.user.internal.domain.Gender;
import com.supportrip.core.system.core.user.internal.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserInfoResponse {
    private Long id;
    private String name;
    private Gender gender;
    private LocalDate birthDay;
    private Role role;
    private String profileImageUrl;
    private boolean isInitialUser;

    @Builder(access = AccessLevel.PRIVATE)
    private UserInfoResponse(Long id, String name, Gender gender, LocalDate birthDay, Role role, String profileImageUrl, boolean isInitialUser) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDay = birthDay;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.isInitialUser = isInitialUser;
    }

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .gender(user.getGender())
                .birthDay(user.getBirthDay())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .isInitialUser(user.isInitialUser())
                .build();
    }
}
