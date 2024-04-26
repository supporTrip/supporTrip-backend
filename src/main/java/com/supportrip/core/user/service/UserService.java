package com.supportrip.core.user.service;

import com.supportrip.core.account.domain.Bank;
import com.supportrip.core.account.domain.LinkedAccount;
import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.account.exception.BankNotFoundException;
import com.supportrip.core.account.exception.LinkedAccountNotFoundException;
import com.supportrip.core.account.repository.BankRepository;
import com.supportrip.core.account.repository.LinkedAccountRepository;
import com.supportrip.core.user.domain.Gender;
import com.supportrip.core.account.repository.PointWalletRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserConsentStatus;
import com.supportrip.core.user.dto.request.SignUpRequest;
import com.supportrip.core.user.dto.response.MyPageProfileResponse;
import com.supportrip.core.user.exception.AlreadySignedUpUserException;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserConsentStatusRepository;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConsentStatusRepository userConsentStatusRepository;
    private final BankRepository bankRepository;
    private final LinkedAccountRepository linkedAccountRepository;
    private final PointWalletRepository pointWalletRepository;

    @Transactional
    public User signUp(Long userId, SignUpRequest request) {
        User user = getUser(userId);

        if (!user.isInitialUser()) {
            throw new AlreadySignedUpUserException();
        }

        user.fillInitialUserInfo(
                request.getName(),
                request.getEmail(),
                request.getGender(),
                request.getPhoneNumber(),
                request.getBirthDay(),
                request.getPinNumber()
        );

        Bank bank = bankRepository.findByCode(request.getBank()).orElseThrow(BankNotFoundException::new);
        LinkedAccount linkedAccount = LinkedAccount.of(user, bank, request.getBankAccountNumber());
        linkedAccountRepository.save(linkedAccount);

        UserConsentStatus userConsentStatus = UserConsentStatus.of(
                user,
                request.getConsentAbove14(),
                request.getServiceTermsConsent(),
                request.getConsentPersonalInfo(),
                request.getAdInfoConsent(),
                request.getMyDataConsentPersonalInfo(),
                request.getOpenBankingAutoTransferConsent(),
                request.getOpenBankingFinancialInfoInquiryConsent(),
                request.getFinancialInfoThirdPartyProvisionConsent(),
                request.getOpenBankingPersonalInfoThirdPartyProvisionConsent(),
                request.getPersonalInfoThirdPartyConsentForESigniture()
        );
        userConsentStatusRepository.save(userConsentStatus);

        PointWallet pointWallet = PointWallet.of(user, 0L);
        pointWalletRepository.save(pointWallet);

        return user;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public MyPageProfileResponse getUserProfile(User user) {
        String profilePic = user.getProfileImageUrl();
        String name = user.getName();
        String email = user.getEmail();
        String birthDate = user.getBirthDay().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String gender = "";
        if(user.getGender() == Gender.MALE) gender = "남자";
        else gender = "여자";
        String registrationDate = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        String phoneNubmber = user.getPhoneNumber();
        LinkedAccount linkedAccount = linkedAccountRepository.findByUser(user).orElseThrow(LinkedAccountNotFoundException::new);
        String bankAccount = linkedAccount.getBank().getName() + " " + linkedAccount.getAccountNumber();

        return MyPageProfileResponse.of(profilePic, name, email, birthDate, gender, registrationDate, phoneNubmber, bankAccount);
    }
}
