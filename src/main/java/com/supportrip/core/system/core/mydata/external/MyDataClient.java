package com.supportrip.core.system.core.mydata.external;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;

import java.util.function.Supplier;

public abstract class MyDataClient {
    protected static final String CLIENT_NAME = "myData";

    private final CircuitBreaker circuitBreaker;
    private final RateLimiter rateLimiter;
    private final Retry retry;

    public MyDataClient(CircuitBreakerRegistry circuitBreakerRegistry,
                        RateLimiterRegistry rateLimiterRegistry,
                        RetryRegistry retryRegistry) {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(CLIENT_NAME);
        this.rateLimiter = rateLimiterRegistry.rateLimiter(CLIENT_NAME);
        this.retry = retryRegistry.retry(CLIENT_NAME);
    }

    protected <R> Supplier<R> decorateCircuitBreakerAndRateLimiterAndRetry(Supplier<R> supplier) {
        Supplier<R> decoratedSupplier = circuitBreaker.decorateSupplier(supplier);
        decoratedSupplier = Retry.decorateSupplier(retry, decoratedSupplier);
        return RateLimiter.decorateSupplier(rateLimiter, decoratedSupplier);
    }
}
