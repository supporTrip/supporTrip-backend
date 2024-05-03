package com.supportrip.core.system.core.exchange.internal.presentation.response;

import com.supportrip.core.system.core.exchange.internal.domain.Country;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ExchangeTradingResponse {
    private final String displayName;
    private final String airplainPnrNumber;
    private final Long tradingAmount;
    private final String baseCurrency;
    private final String baseCountry;
    private final String targetCurrency;
    private final String targetCurrencyCode;
    private final String targetCountry;
    private final String strategy;
    private final Double targetExchangeRate;
    private final LocalDate beganDate;
    private final LocalDate completeDate;


    @Builder(access = AccessLevel.PRIVATE)
    private ExchangeTradingResponse(String displayName,
                                    String airplainPnrNumber,
                                    Long tradingAmount,
                                    String baseCurrency,
                                    String baseCountry,
                                    String targetCurrency,
                                    String targetCurrencyCode,
                                    String targetCountry,
                                    String strategy,
                                    Double targetExchangeRate,
                                    LocalDate beganDate,
                                    LocalDate completeDate
                                    ) {
        this.displayName = displayName;
        this.airplainPnrNumber = airplainPnrNumber;
        this.tradingAmount = tradingAmount;
        this.baseCurrency = baseCurrency;
        this.baseCountry = baseCountry;
        this.targetCurrency = targetCurrency;
        this.targetCurrencyCode = targetCurrencyCode;
        this.targetCountry = targetCountry;
        this.strategy = strategy;
        this.targetExchangeRate = targetExchangeRate;
        this.beganDate = beganDate;
        this.completeDate = completeDate;
    }

    public static ExchangeTradingResponse of(ExchangeTrading exchangeTrading, Country baseCountry, Country targetcountry) {
        return ExchangeTradingResponse.builder()
                .displayName(exchangeTrading.getDisplayName())
                .airplainPnrNumber(exchangeTrading.getAirplaneCertification().getPnrNumber())
                .tradingAmount(exchangeTrading.getTradingAmount())
                .baseCurrency(baseCountry.getCurrency_name())
                .baseCountry(baseCountry.getName())
                .targetCurrency(targetcountry.getCurrency_name())
                .targetCurrencyCode(exchangeTrading.getTargetCurrency().getCode())
                .targetCountry(targetcountry.getName())
                .strategy(exchangeTrading.getStrategy().getName())
                .targetExchangeRate(exchangeTrading.getTargetExchangeRate())
                .beganDate(LocalDate.from(exchangeTrading.getCreatedAt()))
                .completeDate(exchangeTrading.getCompleteDate())
                .build();
    }
}
