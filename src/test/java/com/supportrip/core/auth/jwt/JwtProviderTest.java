package com.supportrip.core.auth.jwt;

import com.supportrip.core.auth.jwt.exception.ExpiredTokenException;
import com.supportrip.core.auth.jwt.exception.InvalidTokenTypeException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("JWT에서 AuthInfo를 가져오는데 성공한다.")
    void parseSuccess() {
        // given
        String validToken = JwtTestSupport.generateToken(new AuthInfo(1L), new Date(), ACCESS_TOKEN_VALID_MILLIS);

        // when
        AuthInfo parsed = jwtProvider.parse(validToken);

        // then
        assertThat(parsed).isNotNull();
        assertThat(parsed.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유효하지 않은 형태의 JWT에서 AuthInfo를 가져올때 예외가 발생한다.")
    void parseFail() {
        // given
        String invalidToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // expected
        assertThatThrownBy(() -> jwtProvider.parse(invalidToken))
                .isInstanceOf(InvalidTokenTypeException.class);
    }

    @Test
    @DisplayName("만료된 JWT에서 AuthInfo를 가져올때 예외가 발생한다.")
    void parseExpiredFail() {
        // given
        Date yesterday = Date.from(Instant.now().minus(1, TimeUnit.DAYS.toChronoUnit()));
        String expiredToken = JwtTestSupport.generateToken(new AuthInfo(1L), yesterday, ACCESS_TOKEN_VALID_MILLIS);

        // expected
        assertThatThrownBy(() -> jwtProvider.parse(expiredToken))
                .isInstanceOf(ExpiredTokenException.class);
    }

    static class JwtTestSupport {
        private static String generateToken(AuthInfo authInfo, Date issuedAt, int tokenValidMillis) {
            Date expiresIn = new Date(issuedAt.getTime() + tokenValidMillis);

            return Jwts.builder()
                    .claims(authInfo.toClaims())
                    .issuedAt(issuedAt)
                    .expiration(expiresIn)
                    .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .compact();
        }
    }
}