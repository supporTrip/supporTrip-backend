package com.supportrip.core.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoPublicKeyResponse {
    private List<PublicKeyResponse> keys;

    public KakaoPublicKeyResponse(List<PublicKeyResponse> keys) {
        this.keys = keys;
    }

    @Getter
    @NoArgsConstructor
    public static class PublicKeyResponse {
        private String kid;
        private String kty;
        private String alg;
        private String use;
        private String n;
        private String e;

        public PublicKeyResponse(String kid, String kty, String alg, String use, String n, String e) {
            this.kid = kid;
            this.kty = kty;
            this.alg = alg;
            this.use = use;
            this.n = n;
            this.e = e;
        }
    }
}
