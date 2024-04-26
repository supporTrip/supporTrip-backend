package com.supportrip.core.exchange.dto;

import com.supportrip.core.exchange.domain.ExchangeRate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CurrentExchangeRateResponse {
    private final String currencyCode;
    private final double exchangeRate;

    @Builder(access = AccessLevel.PRIVATE)
    private CurrentExchangeRateResponse(String currencyCode, double exchangeRate) {
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
    }

    public static CurrentExchangeRateResponse from(ExchangeRate exchangeRate) {
        return CurrentExchangeRateResponse.builder()
                .currencyCode(exchangeRate.getTargetCurrency().getCode())
                .exchangeRate(exchangeRate.getDealBaseRate())
                .build();
    }
}
