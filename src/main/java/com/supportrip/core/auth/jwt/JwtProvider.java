package com.supportrip.core.auth.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.supportrip.core.auth.dto.AuthPayload;
import com.supportrip.core.auth.dto.OidcIdTokenPayload;
import com.supportrip.core.auth.jwt.exception.ExpiredTokenException;
import com.supportrip.core.auth.jwt.exception.InvalidTokenTypeException;
import com.supportrip.core.auth.kakao.KakaoPublicKeyManager;
import com.supportrip.core.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final int accessTokenValidMilliseconds;
    private final int refreshTokenValidMilliseconds;
    private final SecretKey secretKey;

    private final KakaoPublicKeyManager kakaoPublicKeyManager;

    public JwtProvider(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.access-token-valid-millis}") int accessTokenValidMilliseconds,
                       @Value("${jwt.refresh-token-valid-millis}") int refreshTokenValidMilliseconds,
                       KakaoPublicKeyManager kakaoPublicKeyManager) {
        this.accessTokenValidMilliseconds = accessTokenValidMilliseconds;
        this.refreshTokenValidMilliseconds = refreshTokenValidMilliseconds;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.kakaoPublicKeyManager = kakaoPublicKeyManager;
    }

    public String generateAccessToken(AuthPayload authPayload) {
        return generateToken(authPayload, accessTokenValidMilliseconds);
    }

    public String generateRefreshToken(AuthPayload authPayload) {
        return generateToken(authPayload, refreshTokenValidMilliseconds);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(secretKey, token);
            return true;
        } catch (ExpiredJwtException exception) {
            log.info("Expired Token Exception: ", exception);
        } catch (JwtException | IllegalArgumentException exception) {
            log.warn("Invalid Token Exception: ", exception);
        }
        return false;
    }

    public AuthPayload parseAccessToken(String accessToken) {
        return parse(accessToken);
    }

    public AuthPayload parseRefreshToken(String refreshToken) {
        return parse(refreshToken);
    }

    public OidcIdTokenPayload parseIdToken(String idToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getPublicKey(idToken))
                    .build()
                    .parseSignedClaims(idToken)
                    .getPayload();

            return OidcIdTokenPayload.from(claims);
        } catch (ExpiredJwtException exception) {
            log.warn("Expired Token Exception: ", exception);
            throw new ExpiredTokenException();
        } catch (JwtException | IllegalArgumentException | JsonProcessingException exception) {
            log.warn("Invalid Token Type Exception: ", exception);
            throw new InvalidTokenTypeException();
        }
    }

    private AuthPayload parse(String accessToken) {
        try {
            Claims claims = getClaims(secretKey, accessToken);
            return AuthPayload.from(claims);
        } catch (ExpiredJwtException exception) {
            log.warn("Expired Token Exception: ", exception);
            throw new ExpiredTokenException();
        } catch (JwtException | IllegalArgumentException exception) {
            log.warn("Invalid Token Type Exception: ", exception);
            throw new InvalidTokenTypeException();
        }
    }

    private Claims getClaims(SecretKey key, String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private PublicKey getPublicKey(String idToken) throws JsonProcessingException {
        String kid = JwtUtil.extractKidFrom(idToken);
        return kakaoPublicKeyManager.getPublicKey(kid);
    }

    private String generateToken(AuthPayload authPayload, int tokenValidMilliseconds) {
        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + tokenValidMilliseconds);

        return Jwts.builder()
                .claims(authPayload.toClaims())
                .issuedAt(now)
                .expiration(expiresIn)
                .signWith(secretKey)
                .compact();
    }
}
