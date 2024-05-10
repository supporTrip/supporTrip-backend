package com.supportrip.core.system.core.exchange.external.koreaexim;

import com.supportrip.core.system.core.exchange.external.koreaexim.response.KoreaExImExchangeRateResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface KoreaExImExchangeRateAPI {
    String EXCHANGE_RATE_FETCH_URL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON";

    @GetExchange(url = EXCHANGE_RATE_FETCH_URL)
    List<KoreaExImExchangeRateResponse> fetchExchangeRate(@RequestParam String authkey,
                                                          @RequestParam String data,
                                                          @RequestParam(required = false) String searchdate);
}
