package com.supportrip.core.exchange.dto.response;

import com.supportrip.core.exchange.domain.ExchangeTrading;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InProgressExchangeTradingsResponse {
    private final List<ExchangeTrading> inProgressExchanges;

    @Builder(access = AccessLevel.PRIVATE)
    private InProgressExchangeTradingsResponse(List<ExchangeTrading> inProgressExchanges) {
        this.inProgressExchanges = inProgressExchanges;
    }

    public static InProgressExchangeTradingsResponse of(List<ExchangeTrading> exchangeTradings) {
        return InProgressExchangeTradingsResponse.builder()
                .inProgressExchanges(exchangeTradings)
                .build();
    }
}
