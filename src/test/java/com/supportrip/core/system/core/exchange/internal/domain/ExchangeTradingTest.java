package com.supportrip.core.system.core.exchange.internal.domain;

import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import com.supportrip.core.system.core.exchange.internal.domain.TradingStatus;
import com.supportrip.core.context.error.exception.badrequest.AlreadyCompletedTradingException;
import com.supportrip.core.context.error.exception.badrequest.NotEnoughTradingAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExchangeTradingTest {

    @Test
    @DisplayName("환전 거래를 종료한다.")
    void changeToCompleteSuccess() {
        // given
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null, null, null, null, null);

        // when
        exchangeTrading.changeToComplete();

        // then
        assertThat(exchangeTrading.getStatus()).isEqualTo(TradingStatus.COMPLETED);
    }

    @Test
    @DisplayName("이미 종료된 환전 거래를 다시 종료하는 경우 예외가 발생한다.")
    void alreadyChangeToCompleteFail() {
        // given
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null, null, null, null, null);
        exchangeTrading.changeToComplete();

        // expected
        assertThatThrownBy(exchangeTrading::changeToComplete)
                .isInstanceOf(AlreadyCompletedTradingException.class);
    }

    @Test
    @DisplayName("특정 금액만큼 현재 금액에서 차감한다.")
    void reduceAmountSuccess() {
        // given
        final long ENOUGH_AMOUNT = 100_000L;
        final long REDUCE_AMOUNT = 100L;
        ExchangeTrading exchangeTrading =
                ExchangeTrading.of(null, null, null, null, null, null, ENOUGH_AMOUNT, null, null, null);

        // when
        exchangeTrading.reduceAmount(REDUCE_AMOUNT);

        // then
        assertThat(exchangeTrading.getCurrentAmount()).isEqualTo(ENOUGH_AMOUNT - REDUCE_AMOUNT);
    }

    @Test
    @DisplayName("현재 금액보다 많은 금액을 차감하는 경우 예외가 발생한다.")
    void reduceAmountFail() {
        // given
        final long NOT_ENOUGH_AMOUNT = 10L;
        final long REDUCE_AMOUNT = 100L;
        ExchangeTrading exchangeTrading =
                ExchangeTrading.of(null, null, null, null, null, null, NOT_ENOUGH_AMOUNT, null, null, null);

        // expected
        assertThatThrownBy(() -> exchangeTrading.reduceAmount(REDUCE_AMOUNT))
                .isInstanceOf(NotEnoughTradingAmountException.class);
    }

    @Test
    @DisplayName("현재 남은 일수 기준으로 남은 환전 금액을 균등하게 분할한 후 소수점을 버린 금액을 반환한다.")
    void getExchangeAmountSuccess() {
        // given
        final long TRADING_AMOUNT = 1000L;
        final int REMAIN_DAYS = 3;
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null, TRADING_AMOUNT, null, null, null);

        // when
        long exchangeAmount = exchangeTrading.getExchangeAmount(REMAIN_DAYS);

        // then
        assertThat(exchangeAmount).isEqualTo(333L);
    }

    @Test
    @DisplayName("현재 남은 일수가 0보다 작은 경우 예외가 발생한다.")
    void getExchangeAmountFail() {
        // given
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null, null, null, null, null);

        // expected
        assertThatThrownBy(() -> exchangeTrading.getExchangeAmount(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("입력한 환율 기준으로 최대 환전 가능 금액을 소수점을 버린 후 정수로 반환한다.")
    void getMaxExchangeableAmountSuccess() {
        // given
        final long TRADING_AMOUNT = 1000L;
        final double EXCHANGE_RATE = 904.04;
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null, TRADING_AMOUNT, null, null, null);

        // when
        long maxExchangeableAmount = exchangeTrading.getMaxExchangeableCurrencyAmount(EXCHANGE_RATE);

        // then
        assertThat(maxExchangeableAmount).isEqualTo(1);
    }

    @Test
    @DisplayName("입력한 환율이 0보다 작은 경우 예외가 발생한다.")
    void getMaxExchangeableAmountFail() {
        // given
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null, null, null, null, null);

        // expected
        assertThatThrownBy(() -> exchangeTrading.getMaxExchangeableCurrencyAmount(-1.0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("현재 환율 금액을 전부 반환하고, 0으로 설정한다.")
    void flushCurrentAmount() {
        // given
        final long TRADING_AMOUNT = 1000L;
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null, TRADING_AMOUNT, null, null, null);

        // when
        long amount = exchangeTrading.flushCurrentAmount();

        // then
        assertThat(amount).isEqualTo(TRADING_AMOUNT);
        assertThat(exchangeTrading.getCurrentAmount()).isZero();
    }
}