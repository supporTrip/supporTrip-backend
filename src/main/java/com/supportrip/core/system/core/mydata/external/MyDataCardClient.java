package com.supportrip.core.system.core.mydata.external;

import com.supportrip.core.system.core.mydata.external.response.UserCardApprovalListResponse;
import com.supportrip.core.system.core.mydata.external.response.UserCardListResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MyDataCardClient extends MyDataClient {
    private final MyDataCardAPI myDataCardAPI;

    public MyDataCardClient(MyDataCardAPI myDataCardAPI,
                            CircuitBreakerRegistry circuitBreakerRegistry,
                            RetryRegistry retryRegistry) {
        super(circuitBreakerRegistry, retryRegistry);
        this.myDataCardAPI = myDataCardAPI;
    }

    public UserCardListResponse getCardList(String token) {
        return decorateCircuitBreakerAndRateLimiterAndRetry(() -> myDataCardAPI.getCardList(token)).get();
    }

    public UserCardApprovalListResponse getCardApprovalList(Long cardId, String token, LocalDate fromDate, LocalDate toDate) {
        return decorateCircuitBreakerAndRateLimiterAndRetry(() ->
                myDataCardAPI.getCardApprovalList(cardId, token, fromDate, toDate)
        ).get();
    }
}
