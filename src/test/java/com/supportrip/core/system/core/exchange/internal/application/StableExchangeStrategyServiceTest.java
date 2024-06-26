package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.common.external.SmsService;
import com.supportrip.core.system.core.account.internal.application.ForeignAccountService;
import com.supportrip.core.system.core.account.internal.application.PointWalletService;
import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransaction;
import com.supportrip.core.system.core.account.internal.domain.ForeignCurrencyWallet;
import com.supportrip.core.system.core.account.internal.domain.PointWallet;
import com.supportrip.core.system.core.exchange.internal.domain.*;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatus;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static com.supportrip.core.system.core.exchange.internal.domain.TradingStatus.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doAnswer;

@MockitoSettings
class StableExchangeStrategyServiceTest {

    @InjectMocks
    private StableExchangeStrategyService stableExchangeStrategyService;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private ExchangeRateStatisticsService exchangeRateStatisticsService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private PointWalletService pointWalletService;

    @Mock
    private ForeignAccountService foreignAccountService;

    @Mock
    private SmsService smsService;

    @Mock
    private UserNotificationStatusRepository userNotificationStatusRepository;

    @Captor
    private ArgumentCaptor<Long> exchangeAmountCaptor;

    @Test
    @DisplayName("마지막날보다 이전에 실행한 경우 남은일수 기준으로 나눈 금액에서 이전 3개월 평균 환율과 현재 환율을 비교해 차등 구매한다.")
    void executeBeforeLastDay() {
        // given
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        long JAPAN_CURRENCY_UNIT = 100L;
        final LocalDate TODAY = LocalDate.now();
        final LocalDate TOMORROW = TODAY.plusDays(1);
        final long TRADING_AMOUNT = 100_000L;
        final double STARTING_DEAL_BASE_RATE = 1100.0;
        final double DEAL_BASE_RATE = 1200.0;
        final double DEAL_BASE_RATE_AVERAGE = 1150.0;

        ExchangeRate startingExchangeRate = ExchangeRate.of(null, null, null, null, STARTING_DEAL_BASE_RATE);
        ExchangeRate exchangeRate = ExchangeRate.of(null, null, JAPAN_CURRENCY_UNIT, null, DEAL_BASE_RATE);
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, JAPAN_CURRENCY, startingExchangeRate,
                null, null, TRADING_AMOUNT, null, null, TOMORROW);

        ExchangeRateRangeStatistics exchangeRateRangeStatistics =
                ExchangeRateRangeStatistics.of(null, null, null, DEAL_BASE_RATE_AVERAGE);

        given(exchangeRateService.getLatestExchangeRate(any(Currency.class))).willReturn(exchangeRate);
        given(exchangeRateStatisticsService.getExchangeRateAverage(any(Currency.class), any(PeriodUnit.class)))
                .willReturn(exchangeRateRangeStatistics);

        // when
        stableExchangeStrategyService.execute(exchangeTrading, TODAY);

        // then
        verify(exchangeService).exchange(any(ExchangeTrading.class), exchangeAmountCaptor.capture());

        Long exchangeAmount = exchangeAmountCaptor.getValue();
        assertThat(exchangeAmount).isEqualTo(39L);
    }

    @Test
    @DisplayName("마지막날에 실행한 경우 남은 금액 전부 환전하며, 처음 환율과 비교해 손해인 경우 남은 금액에 손해만큼 추가해 포인트를 지급한다.")
    void executeAtLastDay() {
        // given
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        long JAPAN_CURRENCY_UNIT = 100L;
        final LocalDate TODAY = LocalDate.now();
        final long TRADING_AMOUNT = 1300L;
        final double STARTING_DEAL_BASE_RATE = 1100.0;
        final double DEAL_BASE_RATE = 1200.0;

        User user = User.userOf(null, null, null, "010-0000-0000", null, null);
        ExchangeRate startingExchangeRate = ExchangeRate.of(null, null, null, null, STARTING_DEAL_BASE_RATE);
        ExchangeRate exchangeRate = ExchangeRate.of(null, null, JAPAN_CURRENCY_UNIT, null, DEAL_BASE_RATE);
        ExchangeTrading exchangeTrading = ExchangeTrading.of(user, null, JAPAN_CURRENCY, startingExchangeRate,
                null, null, TRADING_AMOUNT, null, null, TODAY);
        PointWallet pointWallet = PointWallet.of(null, 0L);
        UserNotificationStatus userNotificationStatus = UserNotificationStatus.of(user, true);

        given(exchangeRateService.getLatestExchangeRate(any(Currency.class))).willReturn(exchangeRate);

        ForeignCurrencyWallet foreignCurrencyWallet = ForeignCurrencyWallet.of(null, JAPAN_CURRENCY, 500L);
        given(foreignAccountService.getForeignAccountTransactions(any(ExchangeTrading.class)))
                .willReturn(List.of(
                                ForeignAccountTransaction.of(100L, STARTING_DEAL_BASE_RATE, 100L, foreignCurrencyWallet, exchangeTrading),
                                ForeignAccountTransaction.of(200L, STARTING_DEAL_BASE_RATE, 300L, foreignCurrencyWallet, exchangeTrading),
                                ForeignAccountTransaction.of(100L, DEAL_BASE_RATE, 400L, foreignCurrencyWallet, exchangeTrading),
                                ForeignAccountTransaction.of(100L, DEAL_BASE_RATE, 500L, foreignCurrencyWallet, exchangeTrading)
                        )
                );
        given(pointWalletService.getPointWallet(any(User.class))).willReturn(pointWallet);
        given(userNotificationStatusRepository.findByUser(any(User.class))).willReturn(userNotificationStatus);

        final long MAX_EXCHANGEABLE_AMOUNT = 1200L;
        doAnswer(invocation -> {
            ReflectionTestUtils.setField(exchangeTrading, "currentAmount", TRADING_AMOUNT - MAX_EXCHANGEABLE_AMOUNT);
            return null;
        }).when(exchangeService).exchange(any(ExchangeTrading.class), anyLong());

        // when
        stableExchangeStrategyService.execute(exchangeTrading, TODAY);

        // then
        verify(smsService).sendOne(anyString(), anyString());
        verify(exchangeService).exchange(any(ExchangeTrading.class), exchangeAmountCaptor.capture());
        assertThat(exchangeAmountCaptor.getValue()).isEqualTo(1);

        assertThat(exchangeTrading.getStatus()).isEqualTo(COMPLETED);
    }
}