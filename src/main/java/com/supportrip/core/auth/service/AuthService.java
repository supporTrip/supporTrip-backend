package com.supportrip.core.auth.service;

import com.supportrip.core.auth.dto.AuthPayload;
import com.supportrip.core.auth.dto.LoginResponse;
import com.supportrip.core.auth.dto.OidcIdTokenPayload;
import com.supportrip.core.auth.dto.OidcKakaoTokenResponse;
import com.supportrip.core.auth.jwt.JwtProvider;
import com.supportrip.core.auth.jwt.JwtUtil;
import com.supportrip.core.auth.jwt.exception.InvalidTokenTypeException;
import com.supportrip.core.auth.kakao.OidcKakaoAuthClient;
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

        return LoginResponse.of(accessToken, refreshToken, user.isInitialUser());
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
