package com.supportrip.core.auth.dto;

import io.jsonwebtoken.Claims;
import lombok.Getter;

import java.util.Map;

@Getter
public class AuthPayload {
    public static final String USER_ID_KEY = "id";

    private final Long userId;

    public AuthPayload(Long userId) {
        this.userId = userId;
    }

    public Map<String, ?> toClaims() {
        return Map.of(USER_ID_KEY, userId);
    }

    public static AuthPayload from(Claims claims) {
        Long userId = castToLong(claims.get(USER_ID_KEY));
        return new AuthPayload(userId);
    }

    private static Long castToLong(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        return (Long) value;
    }
}
