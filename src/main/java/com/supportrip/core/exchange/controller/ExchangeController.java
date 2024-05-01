package com.supportrip.core.exchange.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.exchange.dto.response.CreateExchangeTradingRequest;
import com.supportrip.core.exchange.dto.response.CurrencyResponse;
import com.supportrip.core.exchange.dto.response.InProgressExchangeTradingsResponse;
import com.supportrip.core.exchange.scheduler.ExchangeRateScheduler;
import com.supportrip.core.exchange.service.ExchangeService;
import com.supportrip.core.log.service.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchange")
public class ExchangeController {
    private final ExchangeService exchangeService;
    private final UserLogService userLogService;
    private final ExchangeRateScheduler exchangeRateScheduler;

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

    @PostMapping("/daily")
    public void doExchange(@RequestBody Map<String, LocalDate> request) {
        LocalDate today = request.get("today");
        exchangeRateScheduler.dailyExchange(today);
    }
}
