package com.supportrip.core.exchange.controller;

import com.supportrip.core.exchange.dto.CurrentExchangeRateResponse;
import com.supportrip.core.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/api/v1/exchange-rates/{currencyId}")
    public CurrentExchangeRateResponse getCurrentExchangeRate(@PathVariable Long currencyId) {
        return exchangeRateService.getCurrentExchangeRate(currencyId);
    }
}
