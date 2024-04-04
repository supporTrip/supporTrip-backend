package com.supportrip.core.auth.jwt;

import lombok.Getter;

import java.util.Map;

@Getter
public class AuthInfo {
    public static final String USER_ID_KEY = "id";

    private Long userId;

    public AuthInfo(Long userId) {
        this.userId = userId;
    }

    public Map<String, Object> toClaims() {
        return Map.of(USER_ID_KEY, userId);
    }
}
