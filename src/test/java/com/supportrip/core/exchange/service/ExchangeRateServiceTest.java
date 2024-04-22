package com.supportrip.core.exchange.service;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.exception.OutdatedExchangeRateException;
import com.supportrip.core.exchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Test
    @DisplayName("특정 통화에 대한 최신 환율 정보를 1개 조회한다.")
    void getLatestExchangeRateSuccess() {
        // given
        final Currency KOREA_CURRENCY = Currency.of(null, "원", "KRW", "₩");
        final Currency JAPAN_CURRENCY = Currency.of(null, "엔", "JPY", "￥");

        ExchangeRate exchangeRate = ExchangeRate.of(JAPAN_CURRENCY, 100L, KOREA_CURRENCY, 1.0);

        LocalDateTime today = LocalDateTime.now();
        ReflectionTestUtils.setField(exchangeRate, "createdAt", today);

        given(exchangeRateRepository.findLatestExchangeByTargetCurrency(any(Currency.class)))
                .willReturn(Optional.of(exchangeRate));

        // when
        ExchangeRate latestExchangeRate = exchangeRateService.getLatestExchangeRate(JAPAN_CURRENCY);

        // then
        assertThat(latestExchangeRate.getBaseCurrency()).isEqualTo(KOREA_CURRENCY);
        assertThat(latestExchangeRate.getTargetCurrency()).isEqualTo(JAPAN_CURRENCY);
        assertThat(latestExchangeRate.getDealBaseRate()).isEqualTo(1.0);
        assertThat(latestExchangeRate.getTargetCurrencyUnit()).isEqualTo(100L);
    }

    @Test
    @DisplayName("특정 통화에 대한 최신 환율 정보가 이전 환율 정보라면 예외가 발생한다.")
    void getOutdatedExchangeRateFail() {
        // given
        final Currency KOREA_CURRENCY = Currency.of(null, "원", "KRW", "₩");
        final Currency JAPAN_CURRENCY = Currency.of(null, "엔", "JPY", "￥");

        ExchangeRate exchangeRate = ExchangeRate.of(KOREA_CURRENCY, 100L, JAPAN_CURRENCY, 1.0);

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        ReflectionTestUtils.setField(exchangeRate, "createdAt", twoDaysAgo);

        given(exchangeRateRepository.findLatestExchangeByTargetCurrency(any(Currency.class)))
                .willReturn(Optional.of(exchangeRate));

        // expected
        assertThatThrownBy(() -> exchangeRateService.getLatestExchangeRate(JAPAN_CURRENCY))
                .isInstanceOf(OutdatedExchangeRateException.class);
    }
}