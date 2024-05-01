package com.supportrip.core.exchange.service;

import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.account.service.PointWalletService;
import com.supportrip.core.common.SmsService;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserNotificationStatus;
import com.supportrip.core.user.repository.UserNotificationStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static com.supportrip.core.exchange.domain.TradingStatus.COMPLETED;
import static com.supportrip.core.exchange.domain.TradingStrategy.TARGET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TargetExchangeStrategyServiceTest {

    @InjectMocks
    private TargetExchangeStrategyService targetExchangeStrategyService;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private PointWalletService pointWalletService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private SmsService smsService;

    @Mock
    private UserNotificationStatusRepository userNotificationStatusRepository;

    @Captor
    private ArgumentCaptor<Long> exchangeAmountCaptor;

    @Test
    @DisplayName("마지막날보다 이전에 실행했으며, 목표 환율에 도달한 경우 모든 금액을 환전한다.")
    void executeBeforeLastDayAndReachedTargetExchangeRate() {
        // given
        User user = User.userOf(null, null, null, null, null, null);
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        final long TRADING_AMOUNT = 12300L;
        final double TARGET_EXCHANGE_RATE = 1000.0;
        final double REACHED_DEAL_BASE_RATE = 950.5;
        final LocalDate TODAY = LocalDate.now();
        final LocalDate TOMORROW = TODAY.plusDays(1);
        final ExchangeRate exchangeRate = ExchangeRate.of(TODAY, JAPAN_CURRENCY, 100L, null, REACHED_DEAL_BASE_RATE);
        ExchangeTrading exchangeTrading =
                ExchangeTrading.of(user, null, JAPAN_CURRENCY, null, null, null, TRADING_AMOUNT, TARGET, TARGET_EXCHANGE_RATE, TOMORROW);

        given(exchangeRateService.getLatestExchangeRate(any(Currency.class))).willReturn(exchangeRate);

        // when
        targetExchangeStrategyService.execute(exchangeTrading, TODAY);

        // then
        verify(exchangeService).exchange(any(ExchangeTrading.class), exchangeAmountCaptor.capture());

        Long exchangeAmount = exchangeAmountCaptor.getValue();
        assertThat(exchangeAmount).isEqualTo(11406L);
    }

    @Test
    @DisplayName("마지막날보다 이전에 실행했으며, 목표 환율에 도달하지 못한 경우 아무런 행동을 하지 않는다.")
    void executeBeforeLastDayAndNotReachedTargetExchangeRate() {
        // given
        User user = User.userOf(null, null, null, null, null, null);
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        final long TRADING_AMOUNT = 12300L;
        final double TARGET_EXCHANGE_RATE = 1000.0;
        final double NOT_REACHED_DEAL_BASE_RATE = 1000.5;
        final LocalDate TODAY = LocalDate.now();
        final LocalDate TOMORROW = TODAY.plusDays(1);
        final ExchangeRate exchangeRate = ExchangeRate.of(TODAY, JAPAN_CURRENCY, 100L, null, NOT_REACHED_DEAL_BASE_RATE);
        ExchangeTrading exchangeTrading =
                ExchangeTrading.of(user, null, JAPAN_CURRENCY, null, null, null, TRADING_AMOUNT, TARGET, TARGET_EXCHANGE_RATE, TOMORROW);

        given(exchangeRateService.getLatestExchangeRate(any(Currency.class))).willReturn(exchangeRate);

        // when
        targetExchangeStrategyService.execute(exchangeTrading, TODAY);

        // then
        verify(exchangeService, never()).exchange(any(ExchangeTrading.class), anyLong());
    }

    @Test
    @DisplayName("마지막날인 경우 모든 금액을 환전하고, 남은 금액을 PointWallet으로 옮긴다.")
    void executeAtLastDay() {
        // given
        User user = User.userOf(null, null, null, "010-0000-0000", null, null);
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");
        final long TRADING_AMOUNT = 12300L;
        final double TARGET_EXCHANGE_RATE = 1000.0;
        final double NOT_REACHED_DEAL_BASE_RATE = 1000.5;
        final LocalDate TODAY = LocalDate.now();
        final ExchangeRate exchangeRate = ExchangeRate.of(TODAY, JAPAN_CURRENCY, 100L, null, NOT_REACHED_DEAL_BASE_RATE);
        ExchangeTrading exchangeTrading =
                ExchangeTrading.of(user, null, JAPAN_CURRENCY, null, null, null, TRADING_AMOUNT, TARGET, TARGET_EXCHANGE_RATE, TODAY);
        PointWallet pointWallet = PointWallet.of(user, 0L);
        UserNotificationStatus userNotificationStatus = UserNotificationStatus.of(user, true);

        given(exchangeRateService.getLatestExchangeRate(any(Currency.class))).willReturn(exchangeRate);
        given(pointWalletService.getPointWallet(any(User.class))).willReturn(pointWallet);
        given(userNotificationStatusRepository.findByUser(any(User.class))).willReturn(userNotificationStatus);

        final long MAX_EXCHANGEABLE_AMOUNT = 12006L;
        doAnswer(invocation -> {
            ReflectionTestUtils.setField(exchangeTrading, "currentAmount", TRADING_AMOUNT - MAX_EXCHANGEABLE_AMOUNT);
            return null;
        }).when(exchangeService).exchange(any(ExchangeTrading.class), anyLong());

        // when
        targetExchangeStrategyService.execute(exchangeTrading, TODAY);

        // then
        verify(smsService).sendOne(anyString(), anyString());
        verify(exchangeService).exchange(any(ExchangeTrading.class), exchangeAmountCaptor.capture());

        Long exchangeAmount = exchangeAmountCaptor.getValue();
        assertThat(exchangeAmount).isEqualTo(MAX_EXCHANGEABLE_AMOUNT);

        assertThat(pointWallet.getTotalAmount()).isEqualTo(294L);
        assertThat(exchangeTrading.getStatus()).isEqualTo(COMPLETED);
    }
}