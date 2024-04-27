package com.supportrip.core.exchange.dto.response;

import com.supportrip.core.exchange.domain.ExchangeTrading;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/*
    title: '첫 휴가 기념 LA 여행',
    ticket: '006 594269C1',
    originCash: '500000',
    originCurrency: '원',
    remainCash: '250000',
    originCentury: '대한민국',
    exchangeCash: '371.47',
    exchangeCurrency: '달러',
    exchangeCentury: '미국',
    createdAt: '2024.03.25',
    endDate: '2024.12.24',
    type: '적극투자형',
*/
@Getter
public class ExchangeTradingResponse {
    private final String displayName;
    private final String airplainPnrNumber;
    private final Long tradingAmount;
    private final String baseCurrency;
    private final String baseCountry;
    private final String targetCurrency;
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
        this.targetCountry = targetCountry;
        this.strategy = strategy;
        this.targetExchangeRate = targetExchangeRate;
        this.beganDate = beganDate;
        this.completeDate = completeDate;
    }

    public static ExchangeTradingResponse of(ExchangeTrading exchangeTrading) {
        return ExchangeTradingResponse.builder()
                .displayName(exchangeTrading.getDisplayName())
                .airplainPnrNumber(exchangeTrading.getAirplainCertification().getPnrNumber())
                .tradingAmount(exchangeTrading.getTradingAmount())
                .baseCurrency(exchangeTrading.getBaseCurrency().getCountry().getCurrency_name())
                .baseCountry(exchangeTrading.getBaseCurrency().getCountry().getName())
                .targetCurrency(exchangeTrading.getTargetCurrency().getCountry().getCurrency_name())
                .targetCountry(exchangeTrading.getTargetCurrency().getCountry().getName())
                .strategy(exchangeTrading.getStrategy().getName())
                .targetExchangeRate(exchangeTrading.getTargetExchangeRate())
                .beganDate(LocalDate.from(exchangeTrading.getCreatedAt()))
                .completeDate(exchangeTrading.getCompleteDate())
                .build();
    }
}
