package com.supportrip.core.system.core.auth.internal.application;

import com.supportrip.core.system.core.auth.external.kakao.KakaoAuthAPI;
import com.supportrip.core.system.core.auth.external.kakao.response.KakaoPublicKeyResponse;
import com.supportrip.core.context.error.exception.internalservererror.KakaoServerException;
import com.supportrip.core.context.error.exception.unauthorized.OAuth2ProcessingException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoPublicKeyManager {
    private final KakaoAuthAPI kakaoAuthAPI;

    private Map<String, PublicKey> cache;
    private LocalDateTime expiresIn;

    @PostConstruct
    void init() {
        KakaoPublicKeyResponse response = getPublicKeyFromKakao();

        this.cache = convertToCacheFrom(response);
        this.expiresIn = LocalDateTime.now().plusDays(1);
    }

    public PublicKey getPublicKey(String kid) {
        if (isExpired()) {
            init();
        }

        if (kid == null || !cache.containsKey(kid)) {
            throw new OAuth2ProcessingException();
        }
        return cache.get(kid);
    }

    private boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return expiresIn.isBefore(now);
    }

    private KakaoPublicKeyResponse getPublicKeyFromKakao() {
        try {
            return kakaoAuthAPI.fetchPublicKey();
        } catch (Exception exception) {
            log.error("Kakao Server Exception: ", exception);
            throw new KakaoServerException();
        }
    }

    private Map<String, PublicKey> convertToCacheFrom(KakaoPublicKeyResponse response) {
        return response.getKeys().stream()
                .collect(Collectors.toMap(
                        KakaoPublicKeyResponse.PublicKeyResponse::getKid,
                        res -> generatePublicKeyFrom(res.getN(), res.getE())
                ));
    }

    private static PublicKey generatePublicKeyFrom(String modulus, String exponent) {
        byte[] modulusBytes = Base64.getUrlDecoder().decode(modulus);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(exponent);

        RSAPublicKeySpec spec = new RSAPublicKeySpec(
                new java.math.BigInteger(1, modulusBytes),
                new java.math.BigInteger(1, exponentBytes)
        );

        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            log.error("Kakao OAuth2 Processing Exception: ", exception);
            throw new OAuth2ProcessingException();
        }
    }

    public Map<String, PublicKey> getCache() {
        return Collections.unmodifiableMap(cache);
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }
}
