package com.supportrip.core.auth.kakao;

import com.supportrip.core.auth.domain.OidcUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class OidcKakaoAuthenticationToken extends AbstractAuthenticationToken {
    private final OidcUser oidcUser;

    public OidcKakaoAuthenticationToken(OidcUser oidcUser) {
        super(oidcUser.getAuthorities());
        this.oidcUser = oidcUser;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return oidcUser;
    }
}
