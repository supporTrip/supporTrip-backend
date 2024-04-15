package com.supportrip.core.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final boolean initialUser;

    @Builder(access = AccessLevel.PRIVATE)
    private LoginResponse(String accessToken, String refreshToken, boolean initialUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.initialUser = initialUser;
    }

    public static LoginResponse of(String accessToken, String refreshToken, boolean initialUser) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .initialUser(initialUser)
                .build();
    }
}
