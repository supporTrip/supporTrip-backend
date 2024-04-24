package com.supportrip.core.user.service;

import com.supportrip.core.user.domain.PhoneVerification;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.exception.AlreadyVerifiedException;
import com.supportrip.core.user.repository.PhoneVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PhoneVerificationServiceTest {

    @InjectMocks
    private PhoneVerificationService phoneVerificationService;

    @Mock
    private UserService userService;

    @Mock
    private PhoneVerificationRepository phoneVerificationRepository;

    @Mock
    private VerificationCodeGenerator verificationCodeGenerator;

    @Test
    @DisplayName("처음 휴대폰 인증을 진행하는 경우 PhoneVerification 객체를 새로 생성해 DB에 저장후 반환한다.")
    void createPhoneVerification() {
        // given
        final Long USER_ID = 1L;
        final String VERIFICATION_CODE = "123123";
        final LocalDateTime NOW = LocalDateTime.now();
        User user = User.userOf(null, null, null, null, null, null);

        given(userService.getUser(anyLong())).willReturn(user);
        given(phoneVerificationRepository.findByUser(any(User.class))).willReturn(Optional.empty());
        given(verificationCodeGenerator.generate()).willReturn(VERIFICATION_CODE);

        // when
        PhoneVerification phoneVerification = phoneVerificationService.createOrRenewPhoneVerification(USER_ID, NOW);

        // then
        verify(phoneVerificationRepository).save(any(PhoneVerification.class));

        assertThat(phoneVerification.getCode()).isEqualTo(VERIFICATION_CODE);
        assertThat(phoneVerification.getExpiresIn()).isEqualTo(NOW.plusMinutes(2));
    }

    @Test
    @DisplayName("이전에 휴대폰 인증을 진행한 적이 있는 경우 PhoneVerification을 갱신한 후 반환한다.")
    void renewPhoneVerification() {
        // given
        final Long USER_ID = 1L;
        final String VERIFICATION_CODE = "123123";
        final String BEFORE_VERIFICATION_CODE = "111111";
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDateTime BEFORE_EXPIRES_IN = NOW.minusSeconds(1);
        User user = User.userOf(null, null, null, null, null, null);
        PhoneVerification phoneVerification = PhoneVerification.of(user, BEFORE_VERIFICATION_CODE, BEFORE_EXPIRES_IN);

        given(userService.getUser(anyLong())).willReturn(user);
        given(phoneVerificationRepository.findByUser(any(User.class))).willReturn(Optional.of(phoneVerification));
        given(verificationCodeGenerator.generate()).willReturn(VERIFICATION_CODE);

        // when
        PhoneVerification renewPhoneVerification = phoneVerificationService.createOrRenewPhoneVerification(USER_ID, NOW);

        // then
        assertThat(renewPhoneVerification.getCode()).isEqualTo(VERIFICATION_CODE);
        assertThat(phoneVerification.getExpiresIn()).isEqualTo(NOW.plusMinutes(2));
    }

    @Test
    @DisplayName("휴대폰 인증이 완료되지 않은 경우 유효한 요청을 보내면 휴대폰 인증에 성공한다.")
    void verifyCodeSuccess() {
        // given
        final Long USER_ID = 1L;
        final String VERIFICATION_CODE = "123123";
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDateTime EXPIRES_IN = NOW.plusMinutes(2);
        User user = User.userOf(null, null, null, null, null, null);
        PhoneVerification phoneVerification = PhoneVerification.of(user, VERIFICATION_CODE, EXPIRES_IN);

        given(userService.getUser(anyLong())).willReturn(user);
        given(phoneVerificationRepository.findByUser(any(User.class))).willReturn(Optional.of(phoneVerification));

        // when
        phoneVerificationService.verifyCode(USER_ID, VERIFICATION_CODE);

        // then
        assertThat(phoneVerification.getVerifiedAt()).isNotNull();
    }

    @Test
    @DisplayName("이미 휴대폰 인증이 완료된 경우 예외가 발생한다.")
    void verifyCodeFail() {
        // given
        final Long USER_ID = 1L;
        final String VERIFICATION_CODE = "123123";
        final LocalDateTime NOW = LocalDateTime.now();
        final LocalDateTime EXPIRES_IN = NOW.plusMinutes(2);
        User user = User.userOf(null, null, null, null, null, null);
        PhoneVerification phoneVerification = PhoneVerification.of(user, VERIFICATION_CODE, EXPIRES_IN);

        ReflectionTestUtils.setField(phoneVerification, "verifiedAt", NOW);

        given(userService.getUser(anyLong())).willReturn(user);
        given(phoneVerificationRepository.findByUser(any(User.class))).willReturn(Optional.of(phoneVerification));

        // expected
        assertThatThrownBy(() -> phoneVerificationService.verifyCode(USER_ID, VERIFICATION_CODE))
                .isInstanceOf(AlreadyVerifiedException.class);
    }
}