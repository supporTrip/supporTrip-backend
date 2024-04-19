package com.supportrip.core.auth.jwt;

import com.supportrip.core.auth.dto.AuthPayload;
import com.supportrip.core.auth.dto.OidcIdTokenPayload;
import com.supportrip.core.auth.jwt.exception.ExpiredTokenException;
import com.supportrip.core.auth.jwt.exception.InvalidTokenTypeException;
import com.supportrip.core.auth.kakao.KakaoPublicKeyManager;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JwtProviderTest {
    private static final String SECRET = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
    private static final int ACCESS_TOKEN_VALID_MILLIS = 300000;
    private static final int REFRESH_TOKEN_VALID_MILLIS = 86400000;

    private static final Long USER_ID = 1L;

    private KakaoPublicKeyManager kakaoPublicKeyManager = mock(KakaoPublicKeyManager.class);

    private final JwtProvider jwtProvider = new JwtProvider(SECRET, ACCESS_TOKEN_VALID_MILLIS, REFRESH_TOKEN_VALID_MILLIS, kakaoPublicKeyManager);

    @Test
    @DisplayName("JWT Access Token을 생성한다.")
    void generateAccessToken() {
        // given
        AuthPayload authPayload = AuthPayload.from(USER_ID);

        // when
        String token = jwtProvider.generateAccessToken(authPayload);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("JWT Refresh Token을 생성한다.")
    void generateRefreshToken() {
        // given
        AuthPayload authPayload = AuthPayload.from(USER_ID);

        // when
        String token = jwtProvider.generateRefreshToken(authPayload);

        // then
        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("JWT에서 AuthPayload를 가져오는데 성공한다.")
    void parseAccessTokenSuccess() {
        // given
        String validToken = JwtTestSupport.generateToken(AuthPayload.from(1L), new Date(), ACCESS_TOKEN_VALID_MILLIS);

        // when
        AuthPayload parsed = jwtProvider.parseAccessToken(validToken);

        // then
        assertThat(parsed).isNotNull();
        assertThat(parsed.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유효하지 않은 형태의 JWT에서 AuthPayload를 가져올때 예외가 발생한다.")
    void parseAccessTokenFail() {
        // given
        String invalidToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaa";

        // expected
        assertThatThrownBy(() -> jwtProvider.parseAccessToken(invalidToken))
                .isInstanceOf(InvalidTokenTypeException.class);
    }

    @Test
    @DisplayName("만료된 JWT에서 AuthPayload를 가져올때 예외가 발생한다.")
    void parseExpiredAccessTokenFail() {
        // given
        Date yesterday = Date.from(Instant.now().minus(1, TimeUnit.DAYS.toChronoUnit()));
        String expiredToken = JwtTestSupport.generateToken(AuthPayload.from(1L), yesterday, ACCESS_TOKEN_VALID_MILLIS);

        // expected
        assertThatThrownBy(() -> jwtProvider.parseAccessToken(expiredToken))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("JWT가 만료되지 않은 경우 true를 반환한다.")
    void validateTokenSuccess() {
        // given
        String validToken = JwtTestSupport.generateToken(AuthPayload.from(1L), new Date(), ACCESS_TOKEN_VALID_MILLIS);

        // when
        boolean isValid = jwtProvider.validateToken(validToken);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("JWT가 만료된 경우 예외가 발생한다.")
    void validateTokenFail() {
        // given
        Date yesterday = Date.from(Instant.now().minus(1, TimeUnit.DAYS.toChronoUnit()));
        String expiredToken = JwtTestSupport.generateToken(AuthPayload.from(1L), yesterday, ACCESS_TOKEN_VALID_MILLIS);

        // expected
        assertThatThrownBy(() -> jwtProvider.validateToken(expiredToken))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("Oidc Id Token으로부터 OidcIdTokenPayload를 생성하여 반환한다.")
    void parseIdTokenSuccess() {
        // given
        final long USER_ID = 1L;
        final String issuer = "https://kauth.kakao.com";
        final String audience = "client_id";
        final String picture = "http://picture";
        final Date today = new Date();
        final Date tomorrow = Date.from(Instant.now().plus(1, TimeUnit.DAYS.toChronoUnit()));

        String idToken = JwtTestSupport.generateIdToken(issuer, audience, Long.toString(USER_ID), today, tomorrow, picture);

        given(kakaoPublicKeyManager.getPublicKey(any())).willReturn(JwtTestSupport.SECURE_KEY_PAIR.getPublic());

        // when
        OidcIdTokenPayload idTokenPayload = jwtProvider.parseIdToken(idToken);

        // then
        long issuedAtSeconds = JwtTestSupport.toSeconds(idTokenPayload.getIat());
        long expirationSeconds = JwtTestSupport.toSeconds(idTokenPayload.getExp());

        assertThat(idTokenPayload).isNotNull();
        assertThat(idTokenPayload.getIss()).isEqualTo(issuer);
        assertThat(idTokenPayload.getAud()).isEqualTo(audience);
        assertThat(idTokenPayload.getSub()).isEqualTo(Long.toString(USER_ID));
        assertThat(issuedAtSeconds).isEqualTo(today.getTime() / 1000);
        assertThat(expirationSeconds).isEqualTo(tomorrow.getTime() / 1000);
        assertThat(idTokenPayload.getAuthTime()).isEqualTo(today.getTime() / 1000 - 1);
        assertThat(idTokenPayload.getPicture()).isEqualTo(picture);
    }

    @Test
    @DisplayName("만료된 Oidc Id Token로부터 OidcIdTokenPayload를 생성하려고 하면 예외를 발생한다.")
    void parseIdTokenExpiredFail() {
        // given
        final long USER_ID = 1L;
        final String issuer = "https://kauth.kakao.com";
        final String audience = "client_id";
        final String picture = "http://picture";
        final Date yesterday = Date.from(Instant.now().minus(1, TimeUnit.DAYS.toChronoUnit()));
        final Date today = new Date();

        String idToken = JwtTestSupport.generateIdToken(issuer, audience, Long.toString(USER_ID), today, yesterday, picture);

        given(kakaoPublicKeyManager.getPublicKey(any())).willReturn(JwtTestSupport.SECURE_KEY_PAIR.getPublic());

        // expected
        assertThatThrownBy(() -> jwtProvider.parseIdToken(idToken))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    @DisplayName("유효하지 않는 형식의 Oidc Id Token로부터 OidcIdTokenPayload를 생성하려고 하면 예외가 발생한다.")
    void parseIdTokenFail() {
        // given
        String idToken = "abcdefghijklmnopqrstuvwxyz";

        // expected
        assertThatThrownBy(() -> jwtProvider.parseIdToken(idToken))
                .isInstanceOf(InvalidTokenTypeException.class);
    }

    static class JwtTestSupport {
        private static final KeyPair SECURE_KEY_PAIR = generateSecureKeyPair();

        private static String generateToken(AuthPayload authPayload, Date issuedAt, int tokenValidMillis) {
            Date expiresIn = new Date(issuedAt.getTime() + tokenValidMillis);

            return Jwts.builder()
                    .claims(authPayload.toClaims())
                    .issuedAt(issuedAt)
                    .expiration(expiresIn)
                    .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .compact();
        }

        private static String generateIdToken(String iss, String aud, String sub, Date iat, Date exp, String picture) {
            Map<String, Object> claims = Map.of("auth_time", iat.getTime() / 1000 - 1, "picture", picture);

            return Jwts.builder()
                    .claims(claims)
                    .audience()
                    .add(aud)
                    .and()
                    .issuer(iss)
                    .subject(sub)
                    .issuedAt(iat)
                    .expiration(exp)
                    .signWith(SECURE_KEY_PAIR.getPrivate(), SignatureAlgorithm.ES256)
                    .compact();
        }

        private static long toSeconds(Date date) {
            return date.getTime() / 1000;
        }

        private static KeyPair generateSecureKeyPair() {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
                ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
                keyPairGenerator.initialize(ecSpec);
                return keyPairGenerator.generateKeyPair();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}