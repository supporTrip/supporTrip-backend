package com.supportrip.core.system.core.user.internal.application;

import com.supportrip.core.context.error.exception.badrequest.AlreadySignedUpUserException;
import com.supportrip.core.system.common.internal.EncryptService;
import com.supportrip.core.system.core.account.internal.domain.Bank;
import com.supportrip.core.system.core.account.internal.domain.BankRepository;
import com.supportrip.core.system.core.account.internal.domain.LinkedAccountRepository;
import com.supportrip.core.system.core.account.internal.domain.PointWalletRepository;
import com.supportrip.core.system.core.user.internal.domain.*;
import com.supportrip.core.system.core.user.internal.presentation.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class UserServiceTest {
    private static final Long USER_ID = 1L;
    private static final String NAME = "가나다";
    private static final String EMAIL = "aaaaa@gmail.com";
    private static final String PHONE_NUMBER = "010-0000-0000";
    private static final String RAW_BIRTH_DAY = "010101";
    private static final LocalDate BIRTH_DAY = LocalDate.of(2001, 1, 1);
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

    @Mock
    private PointWalletRepository pointWalletRepository;

    @Mock
    private UserNotificationStatusRepository userNotificationStatusRepository;

    @Mock
    private EncryptService encryptService;

    @Mock
    private UserCIRepository userCIRepository;

    @Test
    @DisplayName("initialUser가 회원 가입하는 경우 회원 가입에 성공한다.")
    void signUpSuccess() {
        // given
        final String ENCRYPT_PIN_NUMBER = "qwerqwerqwer";

        SignUpRequest request = SignUpRequest.of(NAME, EMAIL, PHONE_NUMBER, RAW_BIRTH_DAY, GENDER, PIN_NUMBER,
                BANK_CODE, BANK_ACCOUNT_NUMBER, CONSENT_ABOVE_14, SERVICE_TERMS_CONSENT, CONSENT_PERSONAL_INFO,
                null, null, OPEN_BANKING_AUTO_TRANSFER_CONSENT,
                OPEN_BANKING_FINANCIAL_INFO_INQUIRY_CONSENT, FINANCIAL_INFO_THIRD_PARTY_PROVISION_CONSENT,
                OPEN_BANKING_PERSONAL_INFO_THIRD_PARTY_PROVISION_CONSENT, PERSONAL_INFO_THIRD_PARTY_CONSENT_FOR_E_SIGNITURE);

        User initialUser = User.initialUserOf(PROFILE_IMAGE_URL);
        Bank bank = Bank.of(BANK_NAME, BANK_CODE, BANK_IMAGE_URL);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(initialUser));
        given(bankRepository.findByCode(anyString())).willReturn(Optional.of(bank));
        given(encryptService.encryptCredentials(anyString())).willReturn(ENCRYPT_PIN_NUMBER);

        // when
        User user = userService.signUp(USER_ID, request);

        // then
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
        assertThat(user.getBirthDay()).isEqualTo(BIRTH_DAY);
        assertThat(user.getGender()).isEqualTo(GENDER);
        assertThat(user.getPinNumber()).isEqualTo(ENCRYPT_PIN_NUMBER);
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