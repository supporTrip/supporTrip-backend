package com.supportrip.core.system.core.exchange.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InProgressExchangeTradingsResponse {
    private final List<ExchangeTradingResponse> inProgressExchanges;

    @Builder(access = AccessLevel.PRIVATE)
    private InProgressExchangeTradingsResponse(List<ExchangeTradingResponse> inProgressExchanges) {
        this.inProgressExchanges = inProgressExchanges;
    }

    public static InProgressExchangeTradingsResponse of(List<ExchangeTradingResponse> inProgressExchangeTradings) {
        return InProgressExchangeTradingsResponse.builder()
                .inProgressExchanges(inProgressExchangeTradings)
                .build();
    }
}
