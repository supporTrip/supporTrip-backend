package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeRateRangeStatistics;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.supportrip.core.exchange.domain.PeriodUnit.THREE_MONTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ExchangeRateStatisticsServiceTest {

    @InjectMocks
    private ExchangeRateStatisticsService exchangeRateStatisticsService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    @DisplayName("특정 통화에 대한 최근 3개월간 평균 환율을 계산해 반환한다.")
    void getLast3MonthExchangeRateAverage() {
        // given
        final Currency KOREA_CURRENCY = Currency.of("원", "KRW", "₩");
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        final LocalDate TODAY = LocalDate.now();
        final LocalDate YESTERDAY = LocalDate.now();
        final LocalDate TWO_DAYS_AGO = LocalDate.now();

        List<ExchangeRate> exchangeRates = List.of(
                ExchangeRate.of(TWO_DAYS_AGO, KOREA_CURRENCY, 100L, JAPAN_CURRENCY, 12.0),
                ExchangeRate.of(YESTERDAY, KOREA_CURRENCY, 100L, JAPAN_CURRENCY, 10.0),
                ExchangeRate.of(TODAY, KOREA_CURRENCY, 100L, JAPAN_CURRENCY, 8.0)
        );
        given(exchangeRateRepository.findByTargetCurrencyAndCreatedAtBetween(any(Currency.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(exchangeRates);

        // when
        ExchangeRateRangeStatistics last3MonthAverage = exchangeRateStatisticsService.getExchangeRateAverage(JAPAN_CURRENCY, THREE_MONTH);

        // then
        assertThat(last3MonthAverage.getExchangeRate()).isEqualTo(10.0);
        assertThat(last3MonthAverage.getTargetCurrency()).isEqualTo(JAPAN_CURRENCY);
    }
}