package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.context.error.exception.internalservererror.OutdatedExchangeRateException;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRateRepository;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeRateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private ExchangeRateService.ExchangeRateInnerService exchangeRateInnerService;

    @Test
    @DisplayName("특정 통화에 대한 최신 환율 정보를 1개 조회한다.")
    void getLatestExchangeRateSuccess() {
        // given
        final Currency KOREA_CURRENCY = Currency.of("원", "KRW", "₩");
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        final LocalDate TODAY = LocalDate.now();

        ExchangeRate exchangeRate = ExchangeRate.of(TODAY, JAPAN_CURRENCY, 100L, KOREA_CURRENCY, 1.0);

        given(exchangeRateRepository.findLatestExchange(any(Currency.class)))
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
    @DisplayName("특정 통화에 대한 최신 환율 정보가 이전 환율 정보라면 환율 정보를 가져와 저장한 후 다시 시도하여 같다면 예외가 발생한다.")
    void getOutdatedExchangeRateFail() {
        // given
        final Currency KOREA_CURRENCY = Currency.of("원", "KRW", "₩");
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        final LocalDate YESTERDAY = LocalDate.now().minusDays(1);

        ExchangeRate exchangeRate = ExchangeRate.of(YESTERDAY, KOREA_CURRENCY, 100L, JAPAN_CURRENCY, 1.0);

        given(exchangeRateRepository.findLatestExchange(any(Currency.class)))
                .willReturn(Optional.of(exchangeRate));

        // expected
        assertThatThrownBy(() -> exchangeRateService.getLatestExchangeRate(JAPAN_CURRENCY))
                .isInstanceOf(OutdatedExchangeRateException.class);

        verify(exchangeRateInnerService).fetchAndStoreExchangeRate(any(LocalDate.class));
    }
}