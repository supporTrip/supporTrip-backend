package com.supportrip.core.exchange.dto.response;

import com.supportrip.core.exchange.domain.ExchangeRate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CurrentExchangeRateResponse {
    private final Long id;
    private final String currencyCode;
    private final double exchangeRate;
    private final Long unit;

    @Builder(access = AccessLevel.PRIVATE)
    private CurrentExchangeRateResponse(Long id, String currencyCode, double exchangeRate, Long unit) {
        this.id = id;
        this.currencyCode = currencyCode;
        this.exchangeRate = exchangeRate;
        this.unit = unit;
    }

    public static CurrentExchangeRateResponse from(ExchangeRate exchangeRate) {
        return CurrentExchangeRateResponse.builder()
                .id(exchangeRate.getId())
                .currencyCode(exchangeRate.getTargetCurrency().getCode())
                .exchangeRate(exchangeRate.getDealBaseRate())
                .unit(exchangeRate.getTargetCurrencyUnit())
                .build();
    }
}
