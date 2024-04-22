package com.supportrip.core.exchange.domain;

import com.supportrip.core.exchange.exception.AlreadyCompletedTradingException;
import com.supportrip.core.exchange.exception.NotEnoughTradingAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExchangeTradingTest {

    @Test
    @DisplayName("환전 거래를 종료한다.")
    void changeToCompleteSuccess() {
        // given
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null);

        // when
        exchangeTrading.changeToComplete();

        // then
        assertThat(exchangeTrading.getStatus()).isEqualTo(TradingStatus.COMPLETED);
    }

    @Test
    @DisplayName("이미 종료된 환전 거래를 다시 종료하는 경우 예외가 발생한다.")
    void alreadyChangeToCompleteFail() {
        // given
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, null, null, null, null);
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
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, ENOUGH_AMOUNT, null, null, null);

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
        ExchangeTrading exchangeTrading = ExchangeTrading.of(null, null, NOT_ENOUGH_AMOUNT, null, null, null);

        // expected
        assertThatThrownBy(() -> exchangeTrading.reduceAmount(REDUCE_AMOUNT))
                .isInstanceOf(NotEnoughTradingAmountException.class);
    }
}