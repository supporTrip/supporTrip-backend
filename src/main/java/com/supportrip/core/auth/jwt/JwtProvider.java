package com.supportrip.core.auth.jwt;

import com.supportrip.core.auth.dto.AuthPayload;
import com.supportrip.core.auth.dto.OidcIdTokenPayload;
import com.supportrip.core.auth.jwt.exception.ExpiredTokenException;
import com.supportrip.core.auth.jwt.exception.InvalidTokenTypeException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.supportrip.core.error.ErrorInfo.EXPIRED_TOKEN;
import static com.supportrip.core.error.ErrorInfo.INVALID_TOKEN_TYPE;

@Slf4j
@Component
public class JwtProvider {
    private final int accessTokenValidMilliseconds;
    private final int refreshTokenValidMilliseconds;
    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.access-token-valid-millis}") int accessTokenValidMilliseconds,
                       @Value("${jwt.refresh-token-valid-millis}") int refreshTokenValidMilliseconds) {
        this.accessTokenValidMilliseconds = accessTokenValidMilliseconds;
        this.refreshTokenValidMilliseconds = refreshTokenValidMilliseconds;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
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
        } catch (MalformedJwtException | IllegalArgumentException | UnsupportedJwtException exception) {
            log.info(INVALID_TOKEN_TYPE.getMessage());
        } catch (ExpiredJwtException exception) {
            log.info(EXPIRED_TOKEN.getMessage());
        }
        return false;
    }

    public AuthPayload parseAccessToken(String accessToken) {
        try {
            Claims claims = getClaims(secretKey, accessToken);
            return AuthPayload.from(claims);
        } catch (MalformedJwtException | IllegalArgumentException | UnsupportedJwtException exception) {
            throw new InvalidTokenTypeException();
        } catch (ExpiredJwtException exception) {
            throw new ExpiredTokenException();
        }
    }

    public OidcIdTokenPayload parseIdToken(String idToken) {
        try {
            Claims claims = getClaims(secretKey, idToken);
            return OidcIdTokenPayload.from(claims);
        } catch (MalformedJwtException | IllegalArgumentException | UnsupportedJwtException exception) {
            throw new InvalidTokenTypeException();
        } catch (ExpiredJwtException exception) {
            throw new ExpiredTokenException();
        }
    }

    private Claims getClaims(SecretKey key, String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
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
