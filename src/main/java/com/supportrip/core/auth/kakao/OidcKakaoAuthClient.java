package com.supportrip.core.auth.kakao;

import com.supportrip.core.auth.dto.OidcKakaoTokenRequest;
import com.supportrip.core.auth.dto.OidcKakaoTokenResponse;
import com.supportrip.core.auth.exception.KakaoServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

@Slf4j
@Component
public class OidcKakaoAuthClient {
    private final KakaoAuthAPI kakaoAuthAPI;

    private final String grantType;
    private final String clientId;
    private final String redirectUri;
    private final String clientSecret;

    public OidcKakaoAuthClient(@Value("${security.oauth2.kakao.authorization-grant-type}") String grantType,
                               @Value("${security.oauth2.kakao.client-id}") String clientId,
                               @Value("${security.oauth2.kakao.redirect-url}") String redirectUri,
                               @Value("${security.oauth2.kakao.client-secret}") String clientSecret,
                               KakaoAuthAPI kakaoAuthAPI) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.clientSecret = clientSecret;
        this.kakaoAuthAPI = kakaoAuthAPI;
    }

    public OidcKakaoTokenResponse getTokenResponse(String code) {
        OidcKakaoTokenRequest tokenRequest = OidcKakaoTokenRequest.from(grantType, clientId, redirectUri, clientSecret, code);

        try {
            return kakaoAuthAPI.fetchToken(tokenRequest.toMultiValueMap());
        } catch (RestClientException exception) {
            log.error("Kakao Server Exception: ", exception);
            throw new KakaoServerException();
        }
    }
}
