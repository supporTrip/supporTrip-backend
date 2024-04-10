package com.supportrip.core.auth.kakao;

import com.supportrip.core.auth.dto.KakaoPublicKeyResponse;
import com.supportrip.core.auth.exception.OAuth2ProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KakaoPublicKeyManagerTest {
    private static final String PUBLIC_KEY_N = "q8zZ0b_MNaLd6Ny8wd4cjFomilLfFIZcmhNSc1ttx_oQdJJZt5CDHB8WWwPGBUDUyY8AmfglS9Y1qA0_fxxs-ZUWdt45jSbUxghKNYgEwSutfM5sROh3srm5TiLW4YfOvKytGW1r9TQEdLe98ork8-rNRYPybRI3SKoqpci1m1QOcvUg4xEYRvbZIWku24DNMSeheytKUz6Ni4kKOVkzfGN11rUj1IrlRR-LNA9V9ZYmeoywy3k066rD5TaZHor5bM5gIzt1B4FmUuFITpXKGQZS5Hn_Ck8Bgc8kLWGAU8TzmOzLeROosqKE0eZJ4ESLMImTb2XSEZuN1wFyL0VtJw";
    private static final String PUBLIC_KEY_E = "AQAB";

    @InjectMocks
    private KakaoPublicKeyManager kakaoPublicKeyManager;

    @Mock
    private KakaoAuthAPI kakaoAuthAPI;

    @Test
    @DisplayName("초기화시 Kakao 서버로부터 Public Key를 가져와 하루간 캐시한다")
    void initialize() {
        // given
        KakaoPublicKeyResponse keyResponse = new KakaoPublicKeyResponse(List.of(
                new KakaoPublicKeyResponse.PublicKeyResponse("kid", "RSA", "RS256", "sig", PUBLIC_KEY_N, PUBLIC_KEY_E)
        ));
        given(kakaoAuthAPI.fetchPublicKey()).willReturn(keyResponse);

        // when
        LocalDateTime before = LocalDateTime.now();
        kakaoPublicKeyManager.init();
        LocalDateTime after = LocalDateTime.now();

        // then
        assertThat(kakaoPublicKeyManager.getExpiresIn()).isAfter(before.plusDays(1));
        assertThat(kakaoPublicKeyManager.getExpiresIn()).isBefore(after.plusDays(1));
        assertThat(kakaoPublicKeyManager.getCache()).hasSize(1);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "invalidKid")
    @DisplayName("Null이나 존재하지 않는 kid 입력시 예외가 발생한다.")
    void getPublicKeyNullFail(String kid) {
        // given
        ReflectionTestUtils.setField(kakaoPublicKeyManager, "expiresIn", LocalDateTime.now().plusDays(1));
        ReflectionTestUtils.setField(kakaoPublicKeyManager, "cache", Map.of());

        // expected
        assertThatThrownBy(() -> kakaoPublicKeyManager.getPublicKey(kid))
                .isInstanceOf(OAuth2ProcessingException.class);
    }

    @Test
    @DisplayName("Public Key 가져올때 캐시 만료된 경우 Kakao 서버로부터 Public Key를 다시 가져와 캐시를 갱신한다.")
    void getPublicKeyExpiredFail() {
        // given
        ReflectionTestUtils.setField(kakaoPublicKeyManager, "expiresIn", LocalDateTime.now().minusDays(1));

        KakaoPublicKeyResponse keyResponse = new KakaoPublicKeyResponse(List.of(
                new KakaoPublicKeyResponse.PublicKeyResponse("kid", "RSA", "RS256", "sig", PUBLIC_KEY_N, PUBLIC_KEY_E)
        ));
        given(kakaoAuthAPI.fetchPublicKey()).willReturn(keyResponse);

        // when
        kakaoPublicKeyManager.getPublicKey("kid");

        // then
        verify(kakaoAuthAPI).fetchPublicKey();
        assertThat(kakaoPublicKeyManager.getExpiresIn()).isAfter(LocalDateTime.now());
    }
}