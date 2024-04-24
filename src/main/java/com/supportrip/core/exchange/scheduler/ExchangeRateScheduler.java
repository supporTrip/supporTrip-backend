package com.supportrip.core.exchange.scheduler;

import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import com.supportrip.core.exchange.repository.ExchangeTradingRepository;
import com.supportrip.core.exchange.scheduler.dto.KoreaExImExchangeRateResponse;
import com.supportrip.core.exchange.service.ExchangeStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.supportrip.core.exchange.domain.TradingStatus.IN_PROGRESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateScheduler {
    private final KoreaExImExchangeRateClient exchangeRateClient;
    private final KoreaExImExchangeRateMapper exchangeRateMapper;
    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeTradingRepository exchangeTradingRepository;
    private final ExchangeStrategyManager exchangeStrategyManager;

    // 매일 12/18시 2번 자동 환전을 진행
    @Transactional
    @Scheduled(cron = "0 0 12/18 ? * MON-FRI")
    public void dailyExchange() {
        LocalDate today = LocalDate.now();
        fetchAndStoreExchangeRate(today);

        List<ExchangeTrading> exchangeTradings = exchangeTradingRepository.findByStatus(IN_PROGRESS);

        log.info("{} ExchangeTradings proceed with currency exchange.", exchangeTradings.size());
        exchangeTradings.forEach(exchangeTrading ->
                exchangeStrategyManager.executeExchangeStrategy(exchangeTrading, today)
        );
    }

    private void fetchAndStoreExchangeRate(LocalDate searchDate) {
        List<KoreaExImExchangeRateResponse> responses = exchangeRateClient.fetchExchangeRate(searchDate);

        if (responses.isEmpty()) {
            throw new IllegalStateException("오늘은 수출입은행으로부터 환율 정보를 가져올 수 없는 일자입니다. 비영업일이거나 영업 시간 이전일 수 있습니다.");
        }

        List<ExchangeRate> exchangeRates = responses.stream()
                .map(response -> exchangeRateMapper.convertEntityFrom(response, searchDate))
                .toList();

        exchangeRateRepository.saveAll(exchangeRates);
    }
}