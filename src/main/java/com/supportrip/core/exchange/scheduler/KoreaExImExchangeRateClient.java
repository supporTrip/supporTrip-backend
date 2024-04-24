package com.supportrip.core.exchange.scheduler;

import com.supportrip.core.exchange.scheduler.dto.KoreaExImExchangeRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class KoreaExImExchangeRateClient {
    private final String authKey;
    private final String data = "AP01";
    private final ExchangeRateAPI exchangeRateAPI;

    public KoreaExImExchangeRateClient(@Value("${open-api.korea-ex-im.auth-key}") String authKey,
                                       ExchangeRateAPI exchangeRateAPI) {
        this.authKey = authKey;
        this.exchangeRateAPI = exchangeRateAPI;
    }

    public List<KoreaExImExchangeRateResponse> fetchExchangeRate(LocalDate searchDate) {
        List<KoreaExImExchangeRateResponse> response = exchangeRateAPI.fetchExchangeRate(authKey, data, searchDate);
        log.info("Fetched {} exchange rates from koreaExIm at {}", response.size(), LocalDateTime.now());
        return response;
    }
}
