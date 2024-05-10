package com.supportrip.core.system.core.auth.internal.application;

import com.supportrip.core.system.core.auth.internal.domain.AuthPayload;
import com.supportrip.core.system.core.auth.internal.presentation.response.LoginResponse;
import com.supportrip.core.system.core.auth.internal.domain.OidcIdTokenPayload;
import com.supportrip.core.system.core.auth.external.kakao.response.OidcKakaoTokenResponse;
import com.supportrip.core.context.error.exception.unauthorized.InvalidTokenTypeException;
import com.supportrip.core.system.core.auth.external.kakao.OidcKakaoAuthClient;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserSocials;
import com.supportrip.core.context.error.exception.notfound.UserSocialsNotFoundException;
import com.supportrip.core.system.core.user.internal.domain.UserRepository;
import com.supportrip.core.system.core.user.internal.domain.UserSocialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.supportrip.core.system.core.user.internal.domain.SocialLoginVender.KAKAO;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final OidcKakaoAuthClient oidcKakaoAuthClient;
    private final UserRepository userRepository;
    private final UserSocialsRepository userSocialsRepository;

    @Transactional
    public LoginResponse login(String code) {
        OidcKakaoTokenResponse tokenResponse = oidcKakaoAuthClient.getTokenResponse(code);
        OidcIdTokenPayload idTokenPayload = jwtProvider.parseIdToken(tokenResponse.getIdToken());
        UserSocials userSocials = getOrSignIn(idTokenPayload);

        // TODO: Oidc 검증 (state & nonce 동일 여부)

        User user = userSocials.getUser();
        AuthPayload authPayload = AuthPayload.from(user.getId());
        String accessToken = jwtProvider.generateAccessToken(authPayload);
        String refreshToken = jwtProvider.generateRefreshToken(authPayload);

        userSocials.replaceRefreshToken(refreshToken);

        return LoginResponse.of(accessToken, refreshToken, user.isInitialUser(), user);
    }

    @Transactional
    public void logout(Long userId) {
        UserSocials userSocials = userSocialsRepository.findByVenderAndUserId(KAKAO, userId)
                .orElseThrow(UserSocialsNotFoundException::new);

        userSocials.clearRefreshToken();
    }

    public String regenerateAccessToken(String authorization) {
        String refreshToken = JwtUtil.extractTokenFrom(authorization);
        AuthPayload authPayload = jwtProvider.parseRefreshToken(refreshToken);

        validateRefreshToken(authPayload, refreshToken);

        return jwtProvider.generateAccessToken(authPayload);
    }

    private void validateRefreshToken(AuthPayload authPayload, String refreshToken) {
        userSocialsRepository.findByVenderAndUserIdAndRefreshToken(KAKAO, authPayload.getUserId(), refreshToken)
                .orElseThrow(InvalidTokenTypeException::new);
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
