package com.supportrip.core.user.service;

import com.supportrip.core.account.domain.Bank;
import com.supportrip.core.account.repository.BankRepository;
import com.supportrip.core.account.repository.LinkedAccountRepository;
import com.supportrip.core.user.domain.Gender;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.dto.SignUpRequest;
import com.supportrip.core.user.exception.AlreadySignedUpUserException;
import com.supportrip.core.user.repository.UserConsentStatusRepository;
import com.supportrip.core.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final String NAME = "가나다";
    private static final String EMAIL = "aaaaa@gmail.com";
    private static final String PHONE_NUMBER = "010-0000-0000";
    private static final LocalDate BIRTH_DAY = LocalDate.now();
    private static final Gender GENDER = Gender.MALE;
    private static final String PIN_NUMBER = "123456";
    private static final String PROFILE_IMAGE_URL = "profile_url";
    private static final Boolean CONSENT_ABOVE_14 = Boolean.TRUE;
    private static final Boolean SERVICE_TERMS_CONSENT = Boolean.TRUE;
    private static final Boolean CONSENT_PERSONAL_INFO = Boolean.TRUE;
    private static final Boolean OPEN_BANKING_AUTO_TRANSFER_CONSENT = Boolean.TRUE;
    private static final Boolean OPEN_BANKING_FINANCIAL_INFO_INQUIRY_CONSENT = Boolean.TRUE;
    private static final Boolean FINANCIAL_INFO_THIRD_PARTY_PROVISION_CONSENT = Boolean.TRUE;
    private static final Boolean OPEN_BANKING_PERSONAL_INFO_THIRD_PARTY_PROVISION_CONSENT = Boolean.TRUE;
    private static final Boolean PERSONAL_INFO_THIRD_PARTY_CONSENT_FOR_E_SIGNITURE = Boolean.TRUE;

    private static final String BANK_NAME = "우리은행";
    private static final String BANK_CODE = "WOORI";
    private static final String BANK_ACCOUNT_NUMBER = "12345678910";
    private static final String BANK_IMAGE_URL = "bank_image_url";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConsentStatusRepository userConsentStatusRepository;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private LinkedAccountRepository linkedAccountRepository;

    @Test
    @DisplayName("initialUser가 회원 가입하는 경우 회원 가입에 성공한다.")
    void signUpSuccess() {
        // given
        SignUpRequest request = SignUpRequest.of(NAME, EMAIL, PHONE_NUMBER, BIRTH_DAY, GENDER, PIN_NUMBER,
                BANK_CODE, BANK_ACCOUNT_NUMBER, CONSENT_ABOVE_14, SERVICE_TERMS_CONSENT, CONSENT_PERSONAL_INFO,
                null, null, OPEN_BANKING_AUTO_TRANSFER_CONSENT,
                OPEN_BANKING_FINANCIAL_INFO_INQUIRY_CONSENT, FINANCIAL_INFO_THIRD_PARTY_PROVISION_CONSENT,
                OPEN_BANKING_PERSONAL_INFO_THIRD_PARTY_PROVISION_CONSENT, PERSONAL_INFO_THIRD_PARTY_CONSENT_FOR_E_SIGNITURE);

        User initialUser = User.initialUserOf(PROFILE_IMAGE_URL);
        Bank bank = Bank.of(BANK_NAME, BANK_CODE, BANK_IMAGE_URL);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(initialUser));
        given(bankRepository.findByCode(anyString())).willReturn(Optional.of(bank));

        // when
        User user = userService.signUp(USER_ID, request);

        // then
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        assertThat(user.getBirthDay()).isEqualTo(BIRTH_DAY);
        assertThat(user.getGender()).isEqualTo(GENDER);
        assertThat(user.getPinNumber()).isEqualTo(PIN_NUMBER);
    }

    @Test
    @DisplayName("이미 회원 가입된 유저가 다시 회원 가입을 진행하려고 하는 경우 예외가 발생한다.")
    void alreadySignedUpFail() {
        // given
        SignUpRequest request = SignUpRequest.of(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        User signedUpUser = User.userOf(NAME, EMAIL, GENDER, PHONE_NUMBER, BIRTH_DAY, PROFILE_IMAGE_URL);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(signedUpUser));

        // expected
        assertThatThrownBy(() -> userService.signUp(USER_ID, request))
                .isInstanceOf(AlreadySignedUpUserException.class);
    }
}