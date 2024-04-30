package com.supportrip.core.exchange.controller;

import com.supportrip.core.auth.domain.OidcUser;
import com.supportrip.core.common.SimpleIdResponse;
import com.supportrip.core.exchange.dto.response.CreateExchangeTradingRequest;
import com.supportrip.core.exchange.dto.response.CurrencyResponse;
import com.supportrip.core.exchange.dto.response.InProgressExchangeTradingsResponse;
import com.supportrip.core.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exchange")
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping("/in-progress")
    public InProgressExchangeTradingsResponse getInProgressExchangeTradings(@AuthenticationPrincipal OidcUser oidcUser) {
        return InProgressExchangeTradingsResponse.of(exchangeService.getInProgressExchangeTradings(oidcUser.getUserId()));
    }

    @PostMapping("/create")
    public SimpleIdResponse createExchangeTrading(@AuthenticationPrincipal OidcUser oidcUser, @RequestBody CreateExchangeTradingRequest request) {
        return SimpleIdResponse.from(exchangeService.createExchangeTrading(oidcUser.getUserId(), request));
    }

    @GetMapping("/currency")
    public CurrencyResponse getExchangeableCurrency() {
        return CurrencyResponse.of(exchangeService.getExchangeableCurrency());
    }
}
