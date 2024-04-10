package com.supportrip.core.auth.util;

import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}