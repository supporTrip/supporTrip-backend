package com.supportrip.core.context.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableFeignClients(basePackages = "com.supportrip.core")
public class FeignClientConfig {
    private static final String CLIENT_NAME = "myData";

    @Bean
    public CircuitBreaker circuitBreaker() {
        return CircuitBreakerRegistry.custom()
                .withCircuitBreakerConfig(
                        CircuitBreakerConfig.custom()
                                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                                .minimumNumberOfCalls(10)
                                .waitDurationInOpenState(Duration.ofMinutes(1))
                                .build()
                )
                .build()
                .circuitBreaker(CLIENT_NAME);
    }

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiterRegistry.custom()
                .withRateLimiterConfig(
                        RateLimiterConfig.custom()
                                .limitRefreshPeriod(Duration.ofSeconds(1))
                                .limitForPeriod(1)
                                .timeoutDuration(Duration.ofMillis(700))
                                .build()
                )
                .build()
                .rateLimiter(CLIENT_NAME);
    }

    @Bean
    public Retry retry() {
        return RetryRegistry.custom()
                .withRetryConfig(
                        RetryConfig.custom()
                                .maxAttempts(3)
                                .waitDuration(Duration.ofMillis(2500))
                                .build()
                )
                .build()
                .retry(CLIENT_NAME);
    }
}
