package com.supportrip.core.user.controller;

import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.account.service.PointWalletService;
import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.common.SmsService;
import com.supportrip.core.insurance.dto.UserInfoResponse;
import com.supportrip.core.log.domain.UserLog;
import com.supportrip.core.log.dto.UserLogListResponse;
import com.supportrip.core.log.service.UserLogService;
import com.supportrip.core.user.domain.PhoneVerification;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.dto.InitiatePhoneVerificationRequest;
import com.supportrip.core.user.dto.VerifyPhoneVerificationCodeRequest;
import com.supportrip.core.user.dto.admin.AdminUserDetailResponse;
import com.supportrip.core.user.dto.admin.AdminUserEnabledUpdateRequest;
import com.supportrip.core.user.dto.admin.AdminUserEnabledUpdatedResponse;
import com.supportrip.core.user.dto.admin.AdminUserResponse;
import com.supportrip.core.user.dto.request.PinNumberVerificationRequest;
import com.supportrip.core.user.dto.request.SignUpRequest;
import com.supportrip.core.user.dto.response.CurrentUserPointResponse;
import com.supportrip.core.user.dto.response.PinNumberVerificationResponse;
import com.supportrip.core.user.service.PhoneVerificationService;
import com.supportrip.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return ResponseEntity.ok(UserInfoResponse.of(user.getName(), user.getGender(), user.getBirthDay()));
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

    @GetMapping("/api/v1/users/{userId}/logs")
    public UserLogListResponse getUserLog(@AuthenticationPrincipal OidcUser oidcUser) {
        List<UserLog> userLogs = userLogService.getUserLogs(oidcUser.getUserId());
        return UserLogListResponse.from(userLogs);
    }

    private String makePhoneVerificationMessage(String code) {
        return String.format("[서포트립] 인증번호는 [%s] 입니다. 본인 확인을 위해 2분 내에 입력해주세요.", code);
    }

    @GetMapping("/api/v1/admin/users")
    public List<AdminUserResponse> adminGetUsersInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        return userService.getUsers(oidcUser.getUserId());
    }

    @GetMapping("/api/v1/admin/users/{id}")
    public AdminUserDetailResponse adminGetUserInfo(@AuthenticationPrincipal OidcUser oidcUser,
                                                    @PathVariable("id") Long id) {
        return userService.getUserInfo(oidcUser.getUserId(), id);
    }

    @PutMapping("/api/v1/admin/users")
    public AdminUserEnabledUpdatedResponse adminUserUpdate(@AuthenticationPrincipal OidcUser oidcUser,
                                                           @RequestBody AdminUserEnabledUpdateRequest request) {
        return userService.userEnabledUpdate(oidcUser.getUserId(), request);
    }
}
