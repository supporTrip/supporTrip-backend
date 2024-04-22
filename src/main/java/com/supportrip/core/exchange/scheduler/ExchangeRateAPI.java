package com.supportrip.core.exchange.scheduler;

import com.supportrip.core.exchange.scheduler.dto.KoreaExImExchangeRateResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDate;
import java.util.List;

public interface ExchangeRateAPI {
    String EXCHANGE_RATE_FETCH_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";

    @GetExchange(url = EXCHANGE_RATE_FETCH_URL)
    List<KoreaExImExchangeRateResponse> fetchExchangeRate(@RequestParam String authkey,
                                                          @RequestParam String data,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchdate);
}
