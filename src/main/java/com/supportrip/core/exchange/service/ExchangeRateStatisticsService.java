package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeRateRangeStatistics;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateStatisticsService {
    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateRangeStatistics getLast3MonthExchangeRateAverage(Currency currency) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before3Month = LocalDateTime.of(now.minusMonths(3).toLocalDate(), LocalTime.MIN);

        return getExchangeRateRangeAverage(currency, before3Month, now);
    }

    private ExchangeRateRangeStatistics getExchangeRateRangeAverage(Currency currency, LocalDateTime startedAt, LocalDateTime endedAt) {
        List<ExchangeRate> last3MonthExchangeRates =
                exchangeRateRepository.findByTargetCurrencyAndCreatedAtBetween(currency, startedAt, endedAt);

        return ExchangeRateRangeStatistics.of(
                startedAt.toLocalDate(),
                endedAt.toLocalDate(),
                currency,
                calculateDealBaseRateAverage(last3MonthExchangeRates)
        );
    }

    private static Double calculateDealBaseRateAverage(List<ExchangeRate> last3MonthExchangeRates) {
        if (last3MonthExchangeRates.isEmpty()) {
            return 0.0;
        }

        Double dealBaseRateSum = last3MonthExchangeRates.stream()
                .map(ExchangeRate::getDealBaseRate)
                .reduce(0.0, Double::sum);

        return dealBaseRateSum / last3MonthExchangeRates.size();
    }
}
