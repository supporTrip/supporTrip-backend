package com.supportrip.core.user.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.common.SmsService;
import com.supportrip.core.insurance.dto.UserInfoResponse;
import com.supportrip.core.user.domain.PhoneVerification;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.dto.InitiatePhoneVerificationRequest;
import com.supportrip.core.user.dto.VerifyPhoneVerificationCodeRequest;
import com.supportrip.core.user.dto.request.SignUpRequest;
import com.supportrip.core.user.dto.request.UserModifiyRequest;
import com.supportrip.core.user.service.PhoneVerificationService;
import com.supportrip.core.user.dto.response.MyPageProfileResponse;
import com.supportrip.core.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SmsService smsService;
    private final PhoneVerificationService phoneVerificationService;

    @PutMapping("/api/v1/users/signup")
    public SimpleIdResponse signUp(@Valid @RequestBody SignUpRequest request,
                                   @AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.signUp(oidcUser.getUserId(), request);
        return SimpleIdResponse.from(user.getId());
    }

    @GetMapping("/api/v1/users")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return ResponseEntity.ok(UserInfoResponse.of(user.getName(), user.getGender() , user.getBirthDay()));
    }
  
    @GetMapping("/api/v1/mypages")
    public MyPageProfileResponse getUserProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return userService.getUserProfile(user);
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

    @PatchMapping("api/v1/mypages")
    public SimpleIdResponse modifiyUserProfile(@AuthenticationPrincipal OidcUser oidcUser, @RequestBody UserModifiyRequest request){
        User user = userService.getUser(oidcUser.getUserId());
        return userService.modifiyUserProfile(user, request);
    }

    private String makePhoneVerificationMessage(String code) {
        return String.format("[서포트립] 인증번호는 [%s] 입니다. 본인 확인을 위해 2분 내에 입력해주세요.", code);
    }
}
