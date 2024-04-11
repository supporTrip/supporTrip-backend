package com.supportrip.core.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportrip.core.auth.jwt.exception.InvalidTokenTypeException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Base64;

@Slf4j
public class JwtUtil {
    private static final String AUTHORIZATION_HEADER_TOKEN_PREFIX = "Bearer ";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String extractTokenFrom(String header) {
        String token = extractTokenWithoutThrow(header);
        if (token == null) {
            throw new InvalidTokenTypeException();
        }
        return token;
    }

    public static String extractTokenWithoutThrow(String header) {
        if (!StringUtils.hasText(header) || !header.startsWith(AUTHORIZATION_HEADER_TOKEN_PREFIX)) {
            return null;
        }
        return header.substring(AUTHORIZATION_HEADER_TOKEN_PREFIX.length());
    }

    public static String extractKidFrom(String idToken) throws JsonProcessingException {
        if (!StringUtils.hasText(idToken)) {
            throw new IllegalArgumentException();
        }

        String tokenHeader = idToken.split("\\.")[0];
        String header = new String(Base64.getDecoder().decode(tokenHeader));

        JwtHeader jwtHeader = objectMapper.readValue(header, JwtHeader.class);
        return jwtHeader.getKid();
    }

    @Getter
    @NoArgsConstructor
    private static class JwtHeader {
        private String kid;
        private String typ;
        private String alg;

        JwtHeader(String kid, String typ, String alg) {
            this.kid = kid;
            this.typ = typ;
            this.alg = alg;
        }
    }
}
