package com.supportrip.core.system.core.auth.external.kakao;

import com.supportrip.core.system.core.auth.external.kakao.response.KakaoPublicKeyResponse;
import com.supportrip.core.system.core.auth.external.kakao.response.OidcKakaoTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoAuthAPI {
    String KAKAO_JWK_URL = "https://kauth.kakao.com/.well-known/jwks.json";
    String KAKAO_OAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";

    @GetExchange(KAKAO_JWK_URL)
    KakaoPublicKeyResponse fetchPublicKey();

    @PostExchange(url = KAKAO_OAUTH_TOKEN_URL, contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    OidcKakaoTokenResponse fetchToken(@RequestBody MultiValueMap body);
}
