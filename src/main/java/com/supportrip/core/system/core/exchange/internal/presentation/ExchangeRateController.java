package com.supportrip.core.system.core.exchange.internal.presentation;

import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRateRangeStatistics;
import com.supportrip.core.system.core.exchange.internal.presentation.response.MinimumExchangeRateResponse;
import com.supportrip.core.system.core.exchange.internal.presentation.response.CurrentExchangeRateResponse;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeRateService;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeRateStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.supportrip.core.system.core.exchange.internal.domain.PeriodUnit.ONE_MONTH;

@RestController
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateStatisticsService exchangeRateStatisticsService;

    @GetMapping("/api/v1/exchange-rates/{currencyId}")
    public CurrentExchangeRateResponse getCurrentExchangeRate(@PathVariable(name = "currencyId") Long currencyId) {
        return exchangeRateService.getCurrentExchangeRate(currencyId);
    }

    @GetMapping("/api/v1/exchange-rates/{currencyId}/minimum")
    public MinimumExchangeRateResponse getMinimumExchangeRate(@PathVariable Long currencyId) {
        ExchangeRateRangeStatistics exchangeRateRangeStatistics =
                exchangeRateStatisticsService.getMinimumExchangeRate(currencyId, ONE_MONTH);

        return MinimumExchangeRateResponse.from(exchangeRateRangeStatistics);
    }
}
