package com.supportrip.core.user.service;

import com.supportrip.core.user.domain.PhoneVerification;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.repository.PhoneVerificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
}