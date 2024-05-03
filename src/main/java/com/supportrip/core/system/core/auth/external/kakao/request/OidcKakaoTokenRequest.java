package com.supportrip.core.system.core.auth.external.kakao.request;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

public class OidcKakaoTokenRequest {
    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;
    private final String code;

    @Builder(access = AccessLevel.PRIVATE)
    private OidcKakaoTokenRequest(String grantType, String clientId, String redirectUri, String clientSecret, String code) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.code = code;
    }

    public static OidcKakaoTokenRequest from(String grantType, String clientId, String redirectUri, String clientSecret, String code) {
        return OidcKakaoTokenRequest.builder()
                .grantType(grantType)
                .clientId(clientId)
                .redirectUri(redirectUri)
                .clientSecret(clientSecret)
                .code(code)
                .build();
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.put("grant_type", Collections.singletonList(grantType));
        multiValueMap.put("client_id", Collections.singletonList(clientId));
        multiValueMap.put("redirect_uri", Collections.singletonList(redirectUri));
        multiValueMap.put("code", Collections.singletonList(code));
        multiValueMap.put("client_secret", Collections.singletonList(clientSecret));

        return multiValueMap;
    }
}
