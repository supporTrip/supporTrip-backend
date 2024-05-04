package com.supportrip.core.system.core.exchange.internal.presentation;

import com.supportrip.core.system.common.internal.SimpleIdResponse;
import com.supportrip.core.system.core.auth.internal.domain.OidcUser;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeService;
import com.supportrip.core.system.core.exchange.internal.presentation.request.CreateExchangeTradingRequest;
import com.supportrip.core.system.core.exchange.internal.presentation.response.CurrencyResponse;
import com.supportrip.core.system.core.exchange.internal.presentation.response.InProgressExchangeTradingsResponse;
import com.supportrip.core.system.core.userlog.internal.application.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchange")
public class ExchangeController {
    private final ExchangeService exchangeService;
    private final UserLogService userLogService;

    @GetMapping("/in-progress")
    public InProgressExchangeTradingsResponse getInProgressExchangeTradings(@AuthenticationPrincipal OidcUser oidcUser) {
        return InProgressExchangeTradingsResponse.of(exchangeService.getInProgressExchangeTradings(oidcUser.getUserId()));
    }

    @PostMapping("/create")
    public SimpleIdResponse createExchangeTrading(@AuthenticationPrincipal OidcUser oidcUser, @RequestBody CreateExchangeTradingRequest request) {
        Long exchangeTradingId = exchangeService.createExchangeTrading(oidcUser.getUserId(), request);
        userLogService.appendUserLog(
                oidcUser.getUserId(),
                "User[ID=" + oidcUser.getUserId() + "] initiated new exchange trading[ID=" + exchangeTradingId + "]."
        );
        return SimpleIdResponse.from(exchangeTradingId);
    }

    @GetMapping("/currency")
    public CurrencyResponse getExchangeableCurrency() {
        return CurrencyResponse.of(exchangeService.getExchangeableCurrency());
    }
}
