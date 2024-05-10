package com.supportrip.core.system.core.exchange.external.koreaexim;

import com.supportrip.core.system.core.exchange.external.koreaexim.response.KoreaExImExchangeRateResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
public class KoreaExImExchangeRateClient {
    private static final String CLIENT_NAME = "koreaExImExchange";

    private final String authKey;
    private final String data = "AP01";
    private final KoreaExImExchangeRateAPI koreaExImExchangeRateAPI;
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public KoreaExImExchangeRateClient(@Value("${open-api.korea-ex-im.auth-key}") String authKey,
                                       KoreaExImExchangeRateAPI koreaExImExchangeRateAPI,
                                       CircuitBreakerRegistry circuitBreakerRegistry,
                                       RetryRegistry retryRegistry) {
        this.authKey = authKey;
        this.koreaExImExchangeRateAPI = koreaExImExchangeRateAPI;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(CLIENT_NAME);
        this.retry = retryRegistry.retry(CLIENT_NAME);
    }

    public List<KoreaExImExchangeRateResponse> fetchExchangeRate(LocalDate searchDate) {
        return decorateCircuitBreakerAndRetry(() -> fetchExchangeRateFromKoreaExIm(searchDate)).get();
    }

    @NotNull
    private List<KoreaExImExchangeRateResponse> fetchExchangeRateFromKoreaExIm(LocalDate searchDate) {
        String searchdate = searchDate != null ? DateTimeFormatter.ISO_LOCAL_DATE.format(searchDate) : null;

        List<KoreaExImExchangeRateResponse> response = koreaExImExchangeRateAPI.fetchExchangeRate(authKey, data, searchdate);
        log.info("Fetched {} exchange rates from koreaExIm at {}", response.size(), LocalDateTime.now());
        return response;
    }

    private <R> Supplier<R> decorateCircuitBreakerAndRetry(Supplier<R> supplier) {
        Supplier<R> decoratedSupplier = circuitBreaker.decorateSupplier(supplier);
        return Retry.decorateSupplier(retry, decoratedSupplier);
    }
}
