package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransaction;
import com.supportrip.core.system.core.account.internal.domain.ForeignCurrencyWallet;
import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransactionRepository;
import com.supportrip.core.system.core.account.internal.application.ForeignAccountService;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import com.supportrip.core.system.core.exchange.internal.domain.TradingStrategy;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeRateService;
import com.supportrip.core.system.core.exchange.internal.application.ExchangeService;
import com.supportrip.core.system.core.user.internal.domain.Gender;
import com.supportrip.core.system.core.user.internal.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {
    private static final String NAME = "가나다";
    private static final String EMAIL = "aaaaa@gmail.com";
    private static final String PHONE_NUMBER = "010-0000-0000";
    private static final LocalDate BIRTH_DAY = LocalDate.now();
    private static final Gender GENDER = Gender.MALE;
    private static final String PROFILE_IMAGE_URL = "profile_url";
    private static final String EXCHANGE_TRADING_NAME = "환전 거래";
    private static final TradingStrategy TRADING_STRATEGY = TradingStrategy.STABLE;

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    private ForeignAccountService foreignAccountService;

    @Mock
    private ForeignAccountTransactionRepository foreignAccountTransactionRepository;

    @Test
    @DisplayName("특정 환전 거래에서 환전할 통화와 금액을 입력하면 환전이 진행된다.")
    void exchangeSuccess() {
        // given
        final long TRADING_AMOUNT = 100_000L;
        final Currency KOREA_CURRENCY = Currency.of("원", "KRW", "₩");
        final Currency JAPAN_CURRENCY = Currency.of("엔", "JPY", "￥");

        User user = User.userOf(NAME, EMAIL, GENDER, PHONE_NUMBER, BIRTH_DAY, PROFILE_IMAGE_URL);
        ExchangeRate exchangeRate = ExchangeRate.of(null, JAPAN_CURRENCY, 100L, KOREA_CURRENCY, 900.0);
        final LocalDate oneMonthLater = LocalDate.now().plusMonths(1);
        ExchangeTrading exchangeTrading =
                ExchangeTrading.of(user, KOREA_CURRENCY, JAPAN_CURRENCY, exchangeRate, null, EXCHANGE_TRADING_NAME, TRADING_AMOUNT, TRADING_STRATEGY, null, oneMonthLater);

        ForeignCurrencyWallet foreignCurrencyWallet = ForeignCurrencyWallet.of(null, JAPAN_CURRENCY, 0L);

        given(exchangeRateService.getLatestExchangeRate(any(Currency.class))).willReturn(exchangeRate);
        given(foreignAccountService.getForeignCurrencyWallet(any(User.class), any(Currency.class))).willReturn(foreignCurrencyWallet);

        // when
        exchangeService.exchange(exchangeTrading, 2L);

        // then
        assertThat(exchangeTrading.getCurrentAmount()).isEqualTo(TRADING_AMOUNT - 1800);
        assertThat(foreignCurrencyWallet.getTotalAmount()).isEqualTo(200L);

        verify(foreignAccountTransactionRepository).save(any(ForeignAccountTransaction.class));
    }
}