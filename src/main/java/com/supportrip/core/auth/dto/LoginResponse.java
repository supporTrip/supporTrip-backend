package com.supportrip.core.auth.dto;

import com.supportrip.core.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final boolean initialUser;
    private final LoginedUserResponse user;

    @Builder(access = AccessLevel.PRIVATE)
    private LoginResponse(String accessToken, String refreshToken, boolean initialUser, LoginedUserResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.initialUser = initialUser;
        this.user = user;
    }

    public static LoginResponse of(String accessToken, String refreshToken, boolean initialUser, User user) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .initialUser(initialUser)
                .user(LoginedUserResponse.from(user))
                .build();
    }
}
