package com.supportrip.core.system.batch;

import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTradingRepository;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeRateService;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.supportrip.core.system.core.exchange.internal.domain.TradingStatus.IN_PROGRESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateScheduler {
    private final ExchangeTradingRepository exchangeTradingRepository;
    private final ExchangeStrategyManager exchangeStrategyManager;
    private final ExchangeRateService.ExchangeRateInnerService exchangeRateService;

    // 매 평일 오후 1시에 자동 환전을 진행
    @Transactional
    @Scheduled(cron = "0 0 13 ? * MON-FRI")
    public void dailyExchange() {
        LocalDate today = LocalDate.now();
        exchangeRateService.fetchAndStoreExchangeRate(today);

        findAndExecuteAutoExchangeInProgress(today);
    }

    public void dailyExchange(LocalDate today) {
        findAndExecuteAutoExchangeInProgress(today);
    }

    private void findAndExecuteAutoExchangeInProgress(LocalDate today) {
        List<ExchangeTrading> exchangeTradings = exchangeTradingRepository.findByStatus(IN_PROGRESS);

        log.info("{} ExchangeTradings proceed with currency exchange.", exchangeTradings.size());
        exchangeTradings.forEach(exchangeTrading -> executeAutoExchange(today, exchangeTrading));
    }

    private void executeAutoExchange(LocalDate today, ExchangeTrading exchangeTrading) {
        try {
            exchangeStrategyManager.executeExchangeStrategy(exchangeTrading, today);
        } catch (Exception exception) {
            log.error("Exchange Exception: ", exception);
        }
    }
}