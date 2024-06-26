package com.supportrip.core.system.core.auth.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegenerateAccessTokenResponse {
    private final String accessToken;

    @Builder(access = AccessLevel.PRIVATE)
    private RegenerateAccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static RegenerateAccessTokenResponse from(String accessToken) {
        return RegenerateAccessTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
