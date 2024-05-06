package com.supportrip.core.system.core.mydata.internal.application;

import com.supportrip.core.system.core.insurance.internal.presentation.request.SendInsuranceRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.response.InsuranceCorporationListResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.response.InsuranceListResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.response.SendInsuranceResponse;
import com.supportrip.core.system.core.mydata.external.InsuranceFeignClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class InsuranceClientService {
    private final InsuranceFeignClient insuranceFeignClient;
    private final CircuitBreaker circuitBreaker;
    private final RateLimiter rateLimiter;

    public InsuranceListResponse getInsured(String token, String orgCode, String code) {
        return decorateCircuitBreakerAndRateLimiter(() -> insuranceFeignClient.getInsured(token, orgCode, code)).get();
    }

    public InsuranceCorporationListResponse getInsuredCorporation(String token) {
        return decorateCircuitBreakerAndRateLimiter(() -> insuranceFeignClient.getInsuredCorporation(token)).get();
    }

    public SendInsuranceResponse sendInsuredTransaction(String token, SendInsuranceRequest request) {
        return decorateCircuitBreakerAndRateLimiter(() ->
                insuranceFeignClient.sendInsuredTransaction(token, request)
        ).get();
    }

    private <R> Supplier<R> decorateCircuitBreakerAndRateLimiter(Supplier<R> supplier) {
        Supplier<R> decoratedSupplier = circuitBreaker.decorateSupplier(supplier);
        return RateLimiter.decorateSupplier(rateLimiter, decoratedSupplier);
    }
}
