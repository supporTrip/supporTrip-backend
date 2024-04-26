package com.supportrip.core.user.service;

import com.supportrip.core.account.domain.*;
import com.supportrip.core.account.dto.request.BankRequest;
import com.supportrip.core.account.dto.response.PointListResponse;
import com.supportrip.core.account.dto.response.PointResponse;
import com.supportrip.core.account.exception.BankNotFoundException;
import com.supportrip.core.account.exception.LinkedAccountNotFoundException;
import com.supportrip.core.account.repository.BankRepository;
import com.supportrip.core.account.repository.LinkedAccountRepository;
import com.supportrip.core.account.service.PointWalletService;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.user.domain.Gender;
import com.supportrip.core.account.repository.PointWalletRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserConsentStatus;
import com.supportrip.core.user.domain.UserNotificationStatus;
import com.supportrip.core.user.dto.request.SignUpRequest;
import com.supportrip.core.user.dto.request.UserModifiyRequest;
import com.supportrip.core.user.dto.response.MyPageProfileResponse;
import com.supportrip.core.user.exception.AlreadySignedUpUserException;
import com.supportrip.core.user.exception.UserNotFoundException;
import com.supportrip.core.user.repository.UserConsentStatusRepository;
import com.supportrip.core.user.repository.UserNotificationStatusRepository;
import com.supportrip.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConsentStatusRepository userConsentStatusRepository;
    private final BankRepository bankRepository;
    private final LinkedAccountRepository linkedAccountRepository;
    private final PointWalletRepository pointWalletRepository;
    private final UserNotificationStatusRepository userNotificationStatusRepository;
    private final PointWalletService pointWalletService;
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

        UserNotificationStatus userNotificationStatus = UserNotificationStatus.of(user, true);
        userNotificationStatusRepository.save(userNotificationStatus);

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
        UserNotificationStatus userNotificationStatus = userNotificationStatusRepository.findByUser(user);
        boolean receiveStatus = userNotificationStatus.getStatus();

        return MyPageProfileResponse.of(profilePic, name, email, birthDate, gender, registrationDate, phoneNubmber, bankAccount, receiveStatus);
    }

    @Transactional
    public SimpleIdResponse modifiyUserProfile(User user, UserModifiyRequest request) {
        LinkedAccount linkedAccount = linkedAccountRepository.findByUser(user).orElseThrow(LinkedAccountNotFoundException::new);
        UserNotificationStatus userNotificationStatus = userNotificationStatusRepository.findByUser(user);

        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getBankAccounts() != null) {
            BankRequest bankRequest = request.getBankAccounts();
            Bank bank = bankRepository.findByCode(bankRequest.getBankCode()).orElseThrow(BankNotFoundException::new);
            linkedAccount.setBank(bank);
            linkedAccount.setAccountNumber(bankRequest.getAccountNum());
            linkedAccount.setTotalAmount(500000L);
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getReceiveStatus() != null) {
            if(request.getReceiveStatus().equals("true"))
                userNotificationStatus.setStatus(true);
            else
                userNotificationStatus.setStatus(false);
        }

        return SimpleIdResponse.from(user.getId());
    }

    public PointListResponse getPointList(User user) {
        Long userTotalPoint = pointWalletService.getPointWallet(user).getTotalAmount();
        List<PointTransaction> pointTransactions = pointWalletService.getPointTransactions(user);

        List<PointResponse> pointResponses = new ArrayList<>();

        for(PointTransaction pointTransaction : pointTransactions){
            String transactionDate = pointTransaction.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            String detail = "";
            String type = "";
            if(pointTransaction.getType() == PointTransactionType.DEPOSIT){
                detail = "포인트 입금";
                type = "+";
            }
            if(pointTransaction.getType() == PointTransactionType.WITHDRAWAL){
                detail = "포인트 출금";
                type = "-";
            }
            Long point = pointTransaction.getAmount();
            Long totalPoint = pointTransaction.getTotalAmount();
            pointResponses.add(PointResponse.of(transactionDate, detail, type, point, totalPoint));
        }

        return PointListResponse.of(userTotalPoint, pointResponses);
    }
}
