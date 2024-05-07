package com.supportrip.core.system.batch.presentation;

import com.supportrip.core.system.batch.ExchangeScheduler;
import com.supportrip.core.system.batch.RemindScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BatchExecuteController {
    private final ExchangeScheduler exchangeScheduler;
    private final RemindScheduler remindScheduler;

    @PostMapping("/api/v1/exchange/daily")
    public void executeExchange(@RequestBody Map<String, LocalDate> request) {
        LocalDate today = request.get("today");
        exchangeScheduler.dailyExchange(today);
    }

    @PostMapping("/api/v1/users/remind")
    public void sendRemindSmsToUserWithMyData() {
        remindScheduler.remindLowCostCurrencyToUser();
    }
}
