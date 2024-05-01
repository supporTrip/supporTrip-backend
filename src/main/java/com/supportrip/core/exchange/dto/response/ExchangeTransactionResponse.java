package com.supportrip.core.exchange.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExchangeTransactionResponse {
    private final String transactionDate;
    private final String name;
    private final String tradingAmount;
    private final String targetAmount;
    private final String targetAvg;

    @Builder(access = AccessLevel.PRIVATE)

    public ExchangeTransactionResponse(String transactionDate, String name, String tradingAmount, String targetAmount, String targetAvg) {
        this.transactionDate = transactionDate;
        this.name = name;
        this.tradingAmount = tradingAmount;
        this.targetAmount = targetAmount;
        this.targetAvg = targetAvg;
    }

    public static ExchangeTransactionResponse of(String transactionDate, String name, String tradingAmount, String targetAmount, String targetAvg){
        return ExchangeTransactionResponse.builder()
                .transactionDate(transactionDate)
                .name(name)
                .tradingAmount(tradingAmount)
                .targetAmount(targetAmount)
                .targetAvg(targetAvg)
                .build();
    }
}
