package com.supportrip.core.exchange.scheduler;

import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import com.supportrip.core.exchange.scheduler.dto.KoreaExImExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
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

    // 수출입은행 Open API로부터 평일 12시, 18시에 환율 정보를 가져와 DB에 저장
    @Scheduled(cron = "0 0 12,18 ? * MON-FRI")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void fetchAndStoreExchangeRate() {
        LocalDate searchDate = LocalDate.now();
        List<KoreaExImExchangeRateResponse> response = exchangeRateClient.fetchExchangeRate(searchDate);

        if (response.isEmpty()) {
            return;
        }

        List<ExchangeRate> exchangeRates = response.stream()
                .map((res) -> exchangeRateMapper.convertEntityFrom(res,searchDate))
                .toList();

        exchangeRateRepository.saveAll(exchangeRates);
    }
}