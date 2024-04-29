package com.supportrip.core.exchange.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ExchangeTransactionListResponse {
    private final List<ExchangeTransactionResponse> exchanges;

    @Builder(access = AccessLevel.PRIVATE)

    public ExchangeTransactionListResponse(List<ExchangeTransactionResponse> exchanges) {
        this.exchanges = exchanges;
    }

    public static ExchangeTransactionListResponse of(List<ExchangeTransactionResponse> exchanges){
        return ExchangeTransactionListResponse.builder()
                .exchanges(exchanges)
                .build();
    }
}
