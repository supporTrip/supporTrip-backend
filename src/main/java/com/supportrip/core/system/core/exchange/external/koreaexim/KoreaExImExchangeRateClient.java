package com.supportrip.core.system.core.exchange.external.koreaexim;

import com.supportrip.core.system.core.exchange.external.koreaexim.response.KoreaExImExchangeRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class KoreaExImExchangeRateClient {
    private final String authKey;
    private final String data = "AP01";
    private final KoreaExImExchangeRateAPI koreaExImExchangeRateAPI;

    public KoreaExImExchangeRateClient(@Value("${open-api.korea-ex-im.auth-key}") String authKey,
                                       KoreaExImExchangeRateAPI koreaExImExchangeRateAPI) {
        this.authKey = authKey;
        this.koreaExImExchangeRateAPI = koreaExImExchangeRateAPI;
    }

    public List<KoreaExImExchangeRateResponse> fetchExchangeRate(LocalDate searchDate) {
        String searchdate = searchDate != null ? DateTimeFormatter.ISO_LOCAL_DATE.format(searchDate) : null;

        List<KoreaExImExchangeRateResponse> response = koreaExImExchangeRateAPI.fetchExchangeRate(authKey, data, searchdate);
        log.info("Fetched {} exchange rates from koreaExIm at {}", response.size(), LocalDateTime.now());
        return response;
    }
}
