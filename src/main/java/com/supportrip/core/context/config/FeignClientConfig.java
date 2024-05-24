package com.supportrip.core.context.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableFeignClients(basePackages = "com.supportrip.core")
public class FeignClientConfig {
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.custom()
                .withCircuitBreakerConfig(
                        CircuitBreakerConfig.custom()
                                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                                .minimumNumberOfCalls(10)
                                .waitDurationInOpenState(Duration.ofMinutes(1))
                                .build()
                )
                .build();
    }

    @Bean
    public RetryRegistry retryRegistry() {
        return RetryRegistry.custom()
                .withRetryConfig(
                        RetryConfig.custom()
                                .maxAttempts(2)
                                .waitDuration(Duration.ofMillis(5000))
                                .build()
                )
                .build();
    }
}
