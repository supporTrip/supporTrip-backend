package com.supportrip.core.auth.service;

import com.supportrip.core.auth.dto.AuthPayload;
import com.supportrip.core.auth.dto.LoginResponse;
import com.supportrip.core.auth.dto.OidcIdTokenPayload;
import com.supportrip.core.auth.dto.OidcKakaoTokenResponse;
import com.supportrip.core.auth.jwt.JwtProvider;
import com.supportrip.core.auth.kakao.OidcKakaoAuthenticationClient;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserSocials;
import com.supportrip.core.user.repository.UserRepository;
import com.supportrip.core.user.repository.UserSocialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.supportrip.core.user.domain.SocialLoginVender.KAKAO;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final OidcKakaoAuthenticationClient oidcKakaoAuthenticationClient;
    private final UserRepository userRepository;
    private final UserSocialsRepository userSocialsRepository;

    @Transactional
    public LoginResponse login(String code) {
        OidcKakaoTokenResponse tokenResponse = oidcKakaoAuthenticationClient.getTokenResponse(code);
        OidcIdTokenPayload idTokenPayload = jwtProvider.parseIdToken(tokenResponse.getIdToken());
        UserSocials userSocials = getOrSignIn(idTokenPayload);

        // TODO: Oidc 검증 (state & nonce 동일 여부)

        Long userId = userSocials.getUser().getId();
        AuthPayload authPayload = AuthPayload.from(userId);
        String accessToken = jwtProvider.generateAccessToken(authPayload);
        String refreshToken = jwtProvider.generateRefreshToken(authPayload);

        userSocials.replaceRefreshToken(refreshToken);

        return LoginResponse.of(accessToken, refreshToken);
    }

    private UserSocials getOrSignIn(OidcIdTokenPayload idTokenPayload) {
        return userSocialsRepository.findByVenderAndSubject(KAKAO, idTokenPayload.getSub())
                .orElseGet(() -> signIn(idTokenPayload));
    }

    private UserSocials signIn(OidcIdTokenPayload idTokenPayload) {
        User user = User.initialUserOf(idTokenPayload.getPicture());
        userRepository.save(user);

        UserSocials userSocials = UserSocials.of(user, KAKAO, idTokenPayload.getSub());
        userSocialsRepository.save(userSocials);

        return userSocials;
    }
}
