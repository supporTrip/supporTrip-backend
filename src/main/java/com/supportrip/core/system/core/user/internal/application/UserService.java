package com.supportrip.core.system.core.user.internal.application;

import com.supportrip.core.context.error.exception.badrequest.AlreadySignedUpUserException;
import com.supportrip.core.context.error.exception.notfound.BankNotFoundException;
import com.supportrip.core.context.error.exception.notfound.LinkedAccountNotFoundException;
import com.supportrip.core.context.error.exception.notfound.UserNotFoundException;
import com.supportrip.core.context.error.exception.notfound.UserNotificationStatusNotFoundException;
import com.supportrip.core.system.common.internal.EncryptService;
import com.supportrip.core.system.common.internal.SimpleIdResponse;
import com.supportrip.core.system.core.account.internal.application.PointWalletService;
import com.supportrip.core.system.core.account.internal.domain.*;
import com.supportrip.core.system.core.account.internal.presentation.request.BankRequest;
import com.supportrip.core.system.core.account.internal.presentation.response.PointTransactionListResponse;
import com.supportrip.core.system.core.account.internal.presentation.response.PointTransactionResponse;
import com.supportrip.core.system.core.user.internal.domain.*;
import com.supportrip.core.system.core.user.internal.presentation.request.AdminUserEnabledUpdateRequest;
import com.supportrip.core.system.core.user.internal.presentation.request.SignUpRequest;
import com.supportrip.core.system.core.user.internal.presentation.request.UserModifiyRequest;
import com.supportrip.core.system.core.user.internal.presentation.response.AdminUserDetailResponse;
import com.supportrip.core.system.core.user.internal.presentation.response.AdminUserEnabledUpdatedResponse;
import com.supportrip.core.system.core.user.internal.presentation.response.AdminUserResponse;
import com.supportrip.core.system.core.user.internal.presentation.response.MyPageProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EncryptService encryptService;
    private final UserCIRepository userCIRepository;

    @Transactional
    public User signUp(User user, SignUpRequest request) {
        if (!user.isInitialUser()) {
            throw new AlreadySignedUpUserException();
        }

        String encryptedPinNumber = encryptService.encryptCredentials(request.getPinNumber());

        user.fillInitialUserInfo(
                request.getName(),
                request.getEmail(),
                request.getGender(),
                request.getPhoneNumber(),
                request.getBirthDay(),
                encryptedPinNumber
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

        String token = encryptService.encryptPhoneNum(request.getPhoneNumber());
        UserCI userCI = UserCI.of(user, token);
        userCIRepository.save(userCI);

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
        if (user.getGender() == Gender.MALE) gender = "남자";
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
            if (request.getBankAccounts().getAccountNum() != null && request.getBankAccounts().getBankCode() != null) {
                BankRequest bankRequest = request.getBankAccounts();
                Bank bank = bankRepository.findByCode(bankRequest.getBankCode()).orElseThrow(BankNotFoundException::new);
                linkedAccount.setBank(bank);
                linkedAccount.setAccountNumber(bankRequest.getAccountNum());
                linkedAccount.setTotalAmount(500000L);
            }
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getReceiveStatus() != null) {
            if (request.getReceiveStatus().equals("true"))
                userNotificationStatus.setStatus(true);
            else
                userNotificationStatus.setStatus(false);
        }

        return SimpleIdResponse.from(user.getId());
    }

    public PointTransactionListResponse getPointList(User user) {
        Long userTotalPoint = pointWalletService.getPointWallet(user).getTotalAmount();
        List<PointTransaction> pointTransactions = pointWalletService.getPointTransactions(user);

        List<PointTransactionResponse> pointTransactionRespons = new ArrayList<>();

        for (PointTransaction pointTransaction : pointTransactions) {
            String transactionDate = pointTransaction.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            String detail = "";
            String type = "";
            if (pointTransaction.getType() == PointTransactionType.DEPOSIT) {
                detail = "포인트 입금";
                type = "+";
            }
            if (pointTransaction.getType() == PointTransactionType.WITHDRAWAL) {
                detail = "포인트 출금";
                type = "-";
            }
            Long point = pointTransaction.getAmount();
            Long totalPoint = pointTransaction.getTotalAmount();
            pointTransactionRespons.add(PointTransactionResponse.of(transactionDate, detail, type, point, totalPoint));
        }

        return PointTransactionListResponse.of(userTotalPoint, pointTransactionRespons);
    }

    public boolean verifyPinNumber(User user, String pinNumber) {
        return encryptService.matchCredentials(user.getPinNumber(), pinNumber);
    }

    /**
     * 관리자 페이지 유저목록 조회
     */
    public List<AdminUserResponse> getUsers() {
        List<AdminUserResponse> adminUserResponses = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail() != null) {
                AdminUserResponse response = AdminUserResponse.of(user);
                adminUserResponses.add(response);
            }
        }
        return adminUserResponses;
    }

    /**
     * 관리자 페이지 유저 세부정보 조회
     */
    public AdminUserDetailResponse getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        UserNotificationStatus status = userNotificationStatusRepository.findById(id).orElseThrow(UserNotificationStatusNotFoundException::new);
        return AdminUserDetailResponse.of(user, status.getStatus());
    }

    @Transactional
    public AdminUserEnabledUpdatedResponse userEnabledUpdate(AdminUserEnabledUpdateRequest request) {
        User user = userRepository.findById(request.getId()).orElseThrow(UserNotFoundException::new);
        user.enabledUpdate(request.isEnabled());
        return AdminUserEnabledUpdatedResponse.of(user);
    }
}
