package com.supportrip.core.exchange.scheduler;

import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import com.supportrip.core.exchange.scheduler.dto.KoreaExImExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateFetchScheduler {
    private final KoreaExImExchangeRateClient exchangeRateClient;
    private final KoreaExImExchangeRateMapper exchangeRateMapper;
    private final ExchangeRateRepository exchangeRateRepository;

    // 수출입은행 Open API로부터 매일 정오에 1번 환율 정보를 가져와 DB에 저장
    @Transactional
    @Scheduled(cron = "0 0 12 ? * MON-FRI")
    public void fetchAndStoreExchangeRate() {
        LocalDate searchDate = LocalDate.now();
        List<KoreaExImExchangeRateResponse> responses = exchangeRateClient.fetchExchangeRate(searchDate);

        if (responses.isEmpty()) {
            return;
        }

        List<ExchangeRate> exchangeRates = responses.stream()
                .map(response -> exchangeRateMapper.convertEntityFrom(response, searchDate))
                .toList();

        exchangeRateRepository.saveAll(exchangeRates);
    }
}