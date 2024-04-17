package com.supportrip.core.auth.dto;

import com.supportrip.core.auth.domain.Role;
import com.supportrip.core.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginedUserResponse {
    private final Long id;
    private final String name;
    private final String email;
    private final String profileImageUrl;
    private final Role role;

    @Builder(access = AccessLevel.PRIVATE)
    private LoginedUserResponse(Long id, String name, String email, String profileImageUrl, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
    }

    public static LoginedUserResponse from(User user) {
        return new LoginedUserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getRole()
        );
    }
}
