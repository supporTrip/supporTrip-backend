package com.supportrip.core.system.core.mydata.internal.application;

import com.supportrip.core.system.core.mydata.external.CardFeignClient;
import com.supportrip.core.system.core.mydata.external.response.UserCardApprovalListResponse;
import com.supportrip.core.system.core.mydata.external.response.UserCardListResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CardClientService {
    private final CardFeignClient cardFeignClient;
    private final CircuitBreaker circuitBreaker;
    private final RateLimiter rateLimiter;
    private final Retry retry;

    public UserCardListResponse getCardList(String token) {
        return decorateCircuitBreakerAndRateLimiter(() -> cardFeignClient.getCardList(token)).get();
    }

    public UserCardApprovalListResponse getCardApprovalList(Long cardId, String token, LocalDate fromDate, LocalDate toDate) {
        return decorateCircuitBreakerAndRateLimiter(() ->
                cardFeignClient.getCardApprovalList(cardId, token, fromDate, toDate)
        ).get();
    }

    private <R> Supplier<R> decorateCircuitBreakerAndRateLimiter(Supplier<R> supplier) {
        Supplier<R> decoratedSupplier = circuitBreaker.decorateSupplier(supplier);
        decoratedSupplier = Retry.decorateSupplier(retry, decoratedSupplier);
        return RateLimiter.decorateSupplier(rateLimiter, decoratedSupplier);
    }
}
