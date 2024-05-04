package com.supportrip.core.system.core.user.internal.presentation;

import com.supportrip.core.system.common.external.SmsService;
import com.supportrip.core.system.common.internal.SimpleIdResponse;
import com.supportrip.core.system.core.account.internal.application.PointWalletService;
import com.supportrip.core.system.core.account.internal.domain.PointWallet;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.insurance.internal.presentation.response.UserInfoResponse;
import com.supportrip.core.system.core.user.internal.application.PhoneVerificationService;
import com.supportrip.core.system.core.user.internal.application.UserService;
import com.supportrip.core.system.core.user.internal.domain.PhoneVerification;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.presentation.request.InitiatePhoneVerificationRequest;
import com.supportrip.core.system.core.user.internal.presentation.request.PinNumberVerificationRequest;
import com.supportrip.core.system.core.user.internal.presentation.request.SignUpRequest;
import com.supportrip.core.system.core.user.internal.presentation.request.VerifyPhoneVerificationCodeRequest;
import com.supportrip.core.system.core.user.internal.presentation.response.CurrentUserPointResponse;
import com.supportrip.core.system.core.user.internal.presentation.response.PinNumberVerificationResponse;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PhoneVerificationService phoneVerificationService;
    private final PointWalletService pointWalletService;
    private final UserLogService userLogService;
    private final SmsService smsService;

    @PutMapping("/api/v1/users/signup")
    public SimpleIdResponse signUp(@Valid @RequestBody SignUpRequest request,
                                   @AuthenticationPrincipal OidcUser oidcUser) {
        Long userId = oidcUser.getUserId();
        User user = userService.signUp(userId, request);
        userLogService.appendUserLog(userId, "Signup successful for the new user: [ID=" + userId + "]");
        return SimpleIdResponse.from(user.getId());
    }

    @GetMapping("/api/v1/users")
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return UserInfoResponse.from(user);
    }

    @PutMapping("/api/v1/users/phone-verification")
    public void initiatePhoneVerification(@AuthenticationPrincipal OidcUser oidcUser,
                                          @RequestBody @Valid InitiatePhoneVerificationRequest request) {
        LocalDateTime now = LocalDateTime.now();
        PhoneVerification phoneVerification =
                phoneVerificationService.createOrRenewPhoneVerification(oidcUser.getUserId(), now);
        smsService.sendOne(makePhoneVerificationMessage(phoneVerification.getCode()), request.getSmsPhoneNumber());
    }

    @PatchMapping("/api/v1/users/phone-verification")
    public void verifyPhoneVerificationCode(@AuthenticationPrincipal OidcUser oidcUser,
                                            @RequestBody @Valid VerifyPhoneVerificationCodeRequest request) {
        phoneVerificationService.verifyCode(oidcUser.getUserId(), request.getCode());
    }

    @PostMapping("/api/v1/users/pin-number/verification")
    public PinNumberVerificationResponse verifyPinNumber(@AuthenticationPrincipal OidcUser oidcUser,
                                                         @RequestBody @Valid PinNumberVerificationRequest request) {
        boolean success = userService.verifyPinNumber(oidcUser.getUserId(), request.getPinNumber());
        return PinNumberVerificationResponse.from(success);
    }

    @GetMapping("/api/v1/users/point")
    public CurrentUserPointResponse getCurrentUserPoint(@AuthenticationPrincipal OidcUser oidcUser) {
        PointWallet pointWallet = pointWalletService.getPointWallet(oidcUser.getUserId());
        return CurrentUserPointResponse.from(pointWallet);
    }

    private String makePhoneVerificationMessage(String code) {
        return String.format("[서포트립] 인증번호는 [%s] 입니다. 본인 확인을 위해 2분 내에 입력해주세요.", code);
    }
}
