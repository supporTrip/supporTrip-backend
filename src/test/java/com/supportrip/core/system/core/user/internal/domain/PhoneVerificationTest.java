package com.supportrip.core.system.core.user.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.PhoneVerification;
import com.supportrip.core.context.error.exception.badrequest.PhoneVerificationFailException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneVerificationTest {

    @Test
    @DisplayName("인증 기간내에 동일한 인증 코드를 입력한 경우 검증에 성공한다.")
    void verifySuccess() {
        // given
        final String VERIFICATION_CODE = "123123";
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDateTime EXPIRES_IN = NOW.plusMinutes(2);
        PhoneVerification phoneVerification = PhoneVerification.of(null, VERIFICATION_CODE, EXPIRES_IN);

        // when
        phoneVerification.verify(VERIFICATION_CODE);

        // then
        assertThat(phoneVerification.getVerifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("인증 기간을 넘어서 검증을 시도한 경우 예외가 발생한다.")
    void verifyExpiredFail() {
        // given
        final String VERIFICATION_CODE = "123123";
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDateTime ALREADY_EXPIRED_TIME = NOW.minusDays(1);
        PhoneVerification phoneVerification = PhoneVerification.of(null, VERIFICATION_CODE, ALREADY_EXPIRED_TIME);

        // expected
        assertThatThrownBy(() -> phoneVerification.verify(VERIFICATION_CODE))
                .isInstanceOf(PhoneVerificationFailException.class);
    }

    @Test
    @DisplayName("입력한 인증 코드가 동일하지 않을 경우 예외가 발생한다.")
    void verifyCodeNotMatchFail() {
        // given
        final String VERIFICATION_CODE = "123123";
        final String NOT_MATCHED_VERIFICATION_CODE = "111111";
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDateTime EXPIRES_IN = NOW.plusMinutes(2);
        PhoneVerification phoneVerification = PhoneVerification.of(null, VERIFICATION_CODE, EXPIRES_IN);

        // expected
        assertThatThrownBy(() -> phoneVerification.verify(NOT_MATCHED_VERIFICATION_CODE))
                .isInstanceOf(PhoneVerificationFailException.class);
    }
}