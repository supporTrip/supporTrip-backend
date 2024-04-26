package com.supportrip.core.exchange.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ExchangeRateRangeAverage {
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Currency targetCurrency;
    private final Double averageRate;

    @Builder(access = AccessLevel.PRIVATE)
    private ExchangeRateRangeAverage(LocalDate startDate, LocalDate endDate, Currency targetCurrency, Double averageRate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetCurrency = targetCurrency;
        this.averageRate = averageRate;
    }

    public static ExchangeRateRangeAverage of(LocalDate startDate, LocalDate endDate, Currency targetCurrency, Double averageRate) {
        return ExchangeRateRangeAverage.builder()
                .startDate(startDate)
                .endDate(endDate)
                .targetCurrency(targetCurrency)
                .averageRate(averageRate)
                .build();
    }
}
