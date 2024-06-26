package com.supportrip.core.system.core.user.internal.presentation;

import com.supportrip.core.system.common.internal.SimpleIdResponse;
import com.supportrip.core.system.core.account.internal.presentation.response.PointTransactionListResponse;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeTradingService;
import com.supportrip.core.system.core.exchange.internal.presentation.response.ExchangeTransactionListResponse;
import com.supportrip.core.system.core.insurance.internal.application.InsuranceService;
import com.supportrip.core.system.core.insurance.internal.presentation.response.InsuranceListResponse;
import com.supportrip.core.system.core.user.internal.application.UserCardService;
import com.supportrip.core.system.core.user.internal.application.UserService;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.presentation.request.UserModifiyRequest;
import com.supportrip.core.system.core.user.internal.presentation.response.MyPageProfileResponse;
import com.supportrip.core.system.core.user.internal.presentation.response.OverseasListResponse;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mypages")
public class MyPageController {
    private final UserService userService;
    private final ExchangeTradingService exchangeTradingService;
    private final InsuranceService insuranceService;
    private final UserLogService userLogService;
    private final UserCardService userCardService;

    @GetMapping
    public MyPageProfileResponse getUserProfile(@AuthenticationPrincipal OidcUser oidcUser) {
        return userService.getUserProfile(oidcUser.getUser());
    }

    @PatchMapping
    public SimpleIdResponse modifiyUserProfile(@AuthenticationPrincipal OidcUser oidcUser,
                                               @RequestBody UserModifiyRequest request) {
        User user = oidcUser.getUser();
        String prevUserInfo = user.toLogMessage();

        SimpleIdResponse response = userService.modifiyUserProfile(user, request);

        userLogService.appendUserLog(
                user.getId(),
                "User[ID=" + user.getId() + "] profile updated: PREV=[" + prevUserInfo + "], UPDATED=[" + user.toLogMessage() + "]"
        );
        return response;
    }

    @GetMapping("/points")
    public PointTransactionListResponse getPointList(@AuthenticationPrincipal OidcUser oidcUser) {
        return userService.getPointList(oidcUser.getUser());
    }

    @GetMapping("/exchanges")
    public ExchangeTransactionListResponse getExchangeList(@AuthenticationPrincipal OidcUser oidcUser) {
        return exchangeTradingService.getExchangeList(oidcUser.getUser());
    }

    @GetMapping("/insurances")
    public InsuranceListResponse getInsuranceList(@AuthenticationPrincipal OidcUser oidcUser) {
        return insuranceService.getInsuranceList(oidcUser.getUser());
    }

    @GetMapping("/overseas")
    public OverseasListResponse getOverseasList(@AuthenticationPrincipal OidcUser oidcUser,
                                                @RequestParam(value = "from_date") LocalDate fromDate,
                                                @RequestParam(value = "to_date") LocalDate toDate) {
        return userCardService.getOverseasList(oidcUser.getUser(), fromDate, toDate);
    }
}
