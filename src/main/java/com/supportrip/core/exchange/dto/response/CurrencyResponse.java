package com.supportrip.core.exchange.dto.response;

import com.supportrip.core.exchange.domain.Currency;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CurrencyResponse {
    private final List<Currency> currencies;

    @Builder(access = AccessLevel.PRIVATE)
    private CurrencyResponse(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public static CurrencyResponse of(List<Currency> currencies) {
        return CurrencyResponse.builder()
                .currencies(currencies)
                .build();
    }
}
