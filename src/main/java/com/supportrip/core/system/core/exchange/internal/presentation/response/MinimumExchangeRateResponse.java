package com.supportrip.core.system.core.exchange.internal.presentation.response;

import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRateRangeStatistics;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MinimumExchangeRateResponse {
    private final String currencyCode;
    private final double minimumExchangeRate;

    @Builder(access = AccessLevel.PRIVATE)
    private MinimumExchangeRateResponse(String currencyCode, double minimumExchangeRate) {
        this.currencyCode = currencyCode;
        this.minimumExchangeRate = minimumExchangeRate;
    }

    public static MinimumExchangeRateResponse from(ExchangeRateRangeStatistics exchangeRateRangeStatistics) {
        return MinimumExchangeRateResponse.builder()
                .currencyCode(exchangeRateRangeStatistics.getTargetCurrency().getCode())
                .minimumExchangeRate(exchangeRateRangeStatistics.getExchangeRate())
                .build();
    }
}
