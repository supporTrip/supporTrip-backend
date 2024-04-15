package com.supportrip.core.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OidcKakaoTokenResponse {
    private String tokenType;
    private String accessToken;
    private String idToken;
    private Long expiresIn;
    private String refreshToken;
    private Long refreshTokenExpiresIn;
    private String scope;

    @Builder(access = AccessLevel.PRIVATE)
    private OidcKakaoTokenResponse(String tokenType, String accessToken, String idToken, Long expiresIn, String refreshToken, Long refreshTokenExpiresIn, String scope) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.idToken = idToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.scope = scope;
    }

    public static OidcKakaoTokenResponse of(String tokenType, String accessToken, String idToken, Long expiresIn, String refreshToken, Long refreshTokenExpiresIn, String scope) {
        return OidcKakaoTokenResponse.builder()
                .tokenType(tokenType)
                .idToken(idToken)
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn)
                .scope(scope)
                .build();
    }
}
