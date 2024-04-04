package com.supportrip.core.auth.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {
    private static final String SECRET = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
    private static final int ACCESS_TOKEN_VALID_MILLIS = 300000;
    private static final int REFRESH_TOKEN_VALID_MILLIS = 86400000;

    private static final Long USER_ID = 1L;

    private JwtProvider jwtProvider = new JwtProvider(SECRET, ACCESS_TOKEN_VALID_MILLIS, REFRESH_TOKEN_VALID_MILLIS);

    @Test
    @DisplayName("JWT Access Token을 생성한다.")
    void generateAccessToken() {
        // given
        AuthInfo authInfo = new AuthInfo(USER_ID);

        // when
        String token = jwtProvider.generateAccessToken(authInfo);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("JWT Refresh Token을 생성한다.")
    void generateRefreshToken() {
        // given
        AuthInfo authInfo = new AuthInfo(USER_ID);

        // when
        String token = jwtProvider.generateRefreshToken(authInfo);

        // then
        System.out.println(token);
        assertThat(token).isNotNull();
    }
}