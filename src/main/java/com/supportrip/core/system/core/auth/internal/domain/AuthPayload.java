package com.supportrip.core.system.core.auth.internal.domain;

import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class AuthPayload {
    public static final String USER_ID_KEY = "id";

    private final Long userId;

    @Builder(access = AccessLevel.PRIVATE)
    private AuthPayload(Long userId) {
        this.userId = userId;
    }

    public Map<String, ?> toClaims() {
        return Map.of(USER_ID_KEY, userId);
    }

    public static AuthPayload from(Claims claims) {
        Long userId = castToLong(claims.get(USER_ID_KEY));
        return AuthPayload.builder()
                .userId(userId)
                .build();
    }

    public static AuthPayload from(Long userId) {
        return AuthPayload.builder()
                .userId(userId)
                .build();
    }

    private static Long castToLong(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        return (Long) value;
    }
}
