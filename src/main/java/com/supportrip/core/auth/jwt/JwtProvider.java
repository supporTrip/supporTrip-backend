package com.supportrip.core.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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

    public String generateAccessToken(AuthInfo authInfo) {
        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + accessTokenValidMilliseconds);

        return Jwts.builder()
                .claims(authInfo.toClaims())
                .issuedAt(now)
                .expiration(expiresIn)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(AuthInfo authInfo) {
        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + refreshTokenValidMilliseconds);

        return Jwts.builder()
                .claims(authInfo.toClaims())
                .issuedAt(now)
                .expiration(expiresIn)
                .signWith(secretKey)
                .compact();
    }
}
