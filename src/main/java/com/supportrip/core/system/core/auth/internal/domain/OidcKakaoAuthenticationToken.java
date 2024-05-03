package com.supportrip.core.system.core.auth.internal.domain;

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

    @Override
    public boolean isAuthenticated() {
        return true;
    }
}
