package com.supportrip.core.system.core.exchange.internal.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ExchangeRateRangeStatistics {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Currency targetCurrency;
    private final Double exchangeRate;

    @Builder(access = AccessLevel.PRIVATE)
    private ExchangeRateRangeStatistics(LocalDate startDate, LocalDate endDate, Currency targetCurrency, Double exchangeRate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetCurrency = targetCurrency;
        this.exchangeRate = exchangeRate;
    }

    public static ExchangeRateRangeStatistics of(LocalDate startDate, LocalDate endDate, Currency targetCurrency, Double exchangeRate) {
        return ExchangeRateRangeStatistics.builder()
                .startDate(startDate)
                .endDate(endDate)
                .targetCurrency(targetCurrency)
                .exchangeRate(exchangeRate)
                .build();
    }
}
