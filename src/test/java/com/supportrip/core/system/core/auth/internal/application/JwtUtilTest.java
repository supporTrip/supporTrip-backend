package com.supportrip.core.system.core.auth.internal.application;

import com.fasterxml.jackson.core.JsonParseException;
import com.supportrip.core.context.error.exception.unauthorized.InvalidTokenTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    @Test
    @DisplayName("JWT의 Header를 분리한 후 kid를 추출하여 반환한다.")
    void extractKidFromSuccess() throws Exception {
        // given
        final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImtleUlkIn0.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcklkIjoxLCJpYXQiOjE1MTYyMzkwMjJ9.2122NdURSi_M2YzzwQH9U-GNH6we85F20ZcwpPQOYPc";

        // when
        String kid = JwtUtil.extractKidFrom(VALID_TOKEN);

        // then
        assertThat(kid).isNotNull();
    }

    @Test
    @DisplayName("Null을 입력할 경우 예외가 발생한다.")
    void extractKidFromNullFail() {
        assertThatThrownBy(() -> JwtUtil.extractKidFrom(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("유효하지 않은 JWT의 Header에서 kid를 추출할 경우 예외가 발생한다.")
    void extractKidFromFail() {
        // given
        final String INVALID_TOKEN = "abcdefghijklmnopqrstuvwxyz";

        // expected
        assertThatThrownBy(() -> JwtUtil.extractKidFrom(INVALID_TOKEN))
                .isInstanceOf(JsonParseException.class);
    }

    @Test
    @DisplayName("정상적인 Bearer token인 경우 token을 추출해 반환한다.")
    void extractTokenSuccess() {
        // given
        final String VALID_BEARER_TOKEN = "Bearer abcdefghijklmnopqrstuvwxyz";

        // when
        String token = JwtUtil.extractTokenFrom(VALID_BEARER_TOKEN);

        // then
        assertThat(token).isEqualTo("abcdefghijklmnopqrstuvwxyz");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "abcdefghijklmnopqrstuvwxyz"})
    @DisplayName("유효하지 않은 Bearer token인 경우 예외가 발생한다.")
    void extractTokenSuccess(String invalidBearerToken) {
        // expected
        assertThatThrownBy(() -> JwtUtil.extractTokenFrom(invalidBearerToken))
                .isInstanceOf(InvalidTokenTypeException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "abcdefghijklmnopqrstuvwxyz"})
    @DisplayName("유효하지 않은 Bearer token인 경우 null을 반환한다.")
    void extractTokenWithoutThrowFail(String invalidBearerToken) {
        // when
        String token = JwtUtil.extractTokenWithoutThrow(invalidBearerToken);

        // then
        assertThat(token).isNull();
    }
}