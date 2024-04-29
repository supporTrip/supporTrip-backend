package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeRateRangeStatistics;
import com.supportrip.core.exchange.domain.PeriodUnit;
import com.supportrip.core.exchange.exception.CurrencyNotFoundException;
import com.supportrip.core.exchange.repository.CurrencyRepository;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ExchangeRateStatisticsService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    public ExchangeRateRangeStatistics getMinimumExchangeRate(Long currencyId, PeriodUnit periodUnit) {
        Currency currency = currencyRepository.findById(currencyId).orElseThrow(CurrencyNotFoundException::new);
        return getMinimumExchangeRate(currency, periodUnit);
    }

    public ExchangeRateRangeStatistics getMinimumExchangeRate(Currency currency, PeriodUnit periodUnit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastDateTime = periodUnit.getPastDateTime(now, true);

        return getExchangeRateRangeStatistics(currency, pastDateTime, now, this::calculateMinimumDealBaseRate);
    }

    public ExchangeRateRangeStatistics getExchangeRateAverage(Currency currency, PeriodUnit periodUnit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pastDateTime = periodUnit.getPastDateTime(now, true);

        return getExchangeRateRangeStatistics(currency, pastDateTime, now, this::calculateDealBaseRateAverage);
    }

    private ExchangeRateRangeStatistics getExchangeRateRangeStatistics(Currency currency,
                                                                       LocalDateTime startedAt,
                                                                       LocalDateTime endedAt,
                                                                       Function<List<ExchangeRate>, Double> calculateFunction) {
        List<ExchangeRate> last3MonthExchangeRates =
                exchangeRateRepository.findByTargetCurrencyAndCreatedAtBetween(currency, startedAt, endedAt);

        return ExchangeRateRangeStatistics.of(
                startedAt.toLocalDate(),
                endedAt.toLocalDate(),
                currency,
                calculateFunction.apply(last3MonthExchangeRates)
        );
    }

    private Double calculateDealBaseRateAverage(List<ExchangeRate> last3MonthExchangeRates) {
        if (last3MonthExchangeRates.isEmpty()) {
            return 0.0;
        }

        Double dealBaseRateSum = last3MonthExchangeRates.stream()
                .map(ExchangeRate::getDealBaseRate)
                .reduce(0.0, Double::sum);

        return dealBaseRateSum / last3MonthExchangeRates.size();
    }

    private Double calculateMinimumDealBaseRate(List<ExchangeRate> last1MonthExchangeRates) {
        if (last1MonthExchangeRates.isEmpty()) {
            return 0.0;
        }

        return last1MonthExchangeRates.stream()
                .map(ExchangeRate::getDealBaseRate)
                .reduce(Double.MAX_VALUE, Double::min);
    }
}
