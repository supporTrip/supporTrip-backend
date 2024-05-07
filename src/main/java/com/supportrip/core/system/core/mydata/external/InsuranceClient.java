package com.supportrip.core.system.core.mydata.external;

import com.supportrip.core.system.core.insurance.internal.presentation.request.SendInsuranceRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.response.InsuranceCorporationListResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.response.InsuranceListResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.response.SendInsuranceResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.stereotype.Service;

@Service
public class InsuranceClient extends MyDataClient {
    private final MyDataInsuranceAPI myDataInsuranceAPI;

    public InsuranceClient(MyDataInsuranceAPI myDataInsuranceAPI,
                           CircuitBreakerRegistry circuitBreakerRegistry,
                           RateLimiterRegistry rateLimiterRegistry,
                           RetryRegistry retryRegistry) {
        super(circuitBreakerRegistry, rateLimiterRegistry, retryRegistry);
        this.myDataInsuranceAPI = myDataInsuranceAPI;
    }

    public InsuranceListResponse getInsured(String token, String orgCode, String code) {
        return decorateCircuitBreakerAndRateLimiterAndRetry(() -> myDataInsuranceAPI.getInsured(token, orgCode, code)).get();
    }

    public InsuranceCorporationListResponse getInsuredCorporation(String token) {
        return decorateCircuitBreakerAndRateLimiterAndRetry(() -> myDataInsuranceAPI.getInsuredCorporation(token)).get();
    }

    public SendInsuranceResponse sendInsuredTransaction(String token, SendInsuranceRequest request) {
        return decorateCircuitBreakerAndRateLimiterAndRetry(() ->
                myDataInsuranceAPI.sendInsuredTransaction(token, request)
        ).get();
    }
}
