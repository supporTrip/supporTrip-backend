package com.supportrip.core.auth.dto;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class OidcIdTokenPayload {
    private final String iss;
    private final String aud;
    private final String sub;
    private final Date iat;
    private final Date exp;
    private final Integer authTime;
    private final String picture;

    @Builder(access = AccessLevel.PRIVATE)
    private OidcIdTokenPayload(String iss, String aud, String sub, Date iat, Date exp, Integer authTime, String picture) {
        this.iss = iss;
        this.aud = aud;
        this.sub = sub;
        this.iat = iat;
        this.exp = exp;
        this.authTime = authTime;
        this.picture = picture;
    }

    public static OidcIdTokenPayload from(Claims claims) {
        List<String> audiences = List.copyOf(claims.getAudience());

        return new OidcIdTokenPayload(
                claims.getIssuer(),
                audiences.get(0),
                claims.getSubject(),
                claims.getIssuedAt(),
                claims.getExpiration(),
                claims.get("auth_time", Integer.class),
                claims.get("picture", String.class)
        );
    }
}
