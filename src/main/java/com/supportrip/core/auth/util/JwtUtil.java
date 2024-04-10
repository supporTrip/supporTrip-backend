package com.supportrip.core.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Base64;

@Slf4j
public class JwtUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
