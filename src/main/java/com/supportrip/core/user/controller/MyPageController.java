package com.supportrip.core.user.controller;

import com.supportrip.core.account.dto.response.PointListResponse;
import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.exchange.dto.response.ExchangeTransactionListResponse;
import com.supportrip.core.exchange.dto.response.ExchangeTransactionResponse;
import com.supportrip.core.exchange.service.ExchangeService;
import com.supportrip.core.exchange.service.ExchangeTradingService;
import com.supportrip.core.insurance.dto.InsuranceListResponse;
import com.supportrip.core.insurance.service.InsuranceService;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.dto.request.UserModifiyRequest;
import com.supportrip.core.user.dto.response.MyPageProfileResponse;
import com.supportrip.core.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypages")
public class MyPageController {

    private final UserService userService;
    private final ExchangeTradingService exchangeTradingService;
    private final InsuranceService insuranceService;

    @GetMapping
    public MyPageProfileResponse getUserProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = userService.getUser(oidcUser.getUserId());
        return userService.getUserProfile(user);
    }


    @PatchMapping
    public SimpleIdResponse modifiyUserProfile(@AuthenticationPrincipal OidcUser oidcUser, @RequestBody UserModifiyRequest request){
        User user = userService.getUser(oidcUser.getUserId());
        return userService.modifiyUserProfile(user, request);
    }

    @GetMapping("/points")
    public PointListResponse getPointList(@AuthenticationPrincipal OidcUser oidcUser){
        User user = userService.getUser(oidcUser.getUserId());
        return userService.getPointList(user);
    }

    @GetMapping("/exchanges")
    public ExchangeTransactionListResponse getExchangeList(@AuthenticationPrincipal OidcUser oidcUser){
        User user = userService.getUser(oidcUser.getUserId());
        return exchangeTradingService.getExchangeList(user);
    }

    @GetMapping("/insurances")
    public InsuranceListResponse getInsuranceList(@AuthenticationPrincipal OidcUser oidcUser){
        User user = userService.getUser(oidcUser.getUserId());
        return insuranceService.getInsuranceList(user);
    }
}
