package com.supportrip.core.auth.kakao;

import com.supportrip.core.auth.dto.KakaoPublicKeyResponse;
import com.supportrip.core.auth.dto.OidcKakaoTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoAuthAPI {
    String KAKAO_JWK_URL = "https://kauth.kakao.com/.well-known/jwks.json";

    @GetExchange(KAKAO_JWK_URL)
    KakaoPublicKeyResponse fetchPublicKey();
}
