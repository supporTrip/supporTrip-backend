package com.supportrip.core.system.core.exchange.internal.application;

import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransaction;
import com.supportrip.core.system.core.account.internal.domain.PointWallet;
import com.supportrip.core.system.core.account.internal.application.ForeignAccountService;
import com.supportrip.core.system.core.account.internal.application.PointWalletService;
import com.supportrip.core.system.common.external.SmsService;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRate;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeRateRangeStatistics;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatus;
import com.supportrip.core.system.core.user.internal.domain.UserNotificationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.supportrip.core.system.core.exchange.internal.domain.PeriodUnit.THREE_MONTH;
import static com.supportrip.core.system.core.exchange.internal.domain.TradingStrategy.STABLE;

@Service
@RequiredArgsConstructor
public class StableExchangeStrategyService implements ExchangeStrategyService {
    private final ExchangeService exchangeService;
    private final ExchangeRateStatisticsService exchangeRateStatisticsService;
    private final ExchangeRateService exchangeRateService;
    private final PointWalletService pointWalletService;
    private final ForeignAccountService foreignAccountService;
    private final SmsService smsService;
    private final UserNotificationStatusRepository userNotificationStatusRepository;

    @Override
    @Transactional
    public void execute(ExchangeTrading exchangeTrading, LocalDate today) {
        Currency targetCurrency = exchangeTrading.getTargetCurrency();
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate(targetCurrency);

        if (exchangeTrading.isLastDate(today)) {
            long maxExchangeableAmount = exchangeTrading.getMaxExchangeableCurrencyAmount(exchangeRate.getDealBaseRate());
            exchangeService.exchange(exchangeTrading, maxExchangeableAmount);

            List<ForeignAccountTransaction> foreignAccountTransactions =
                    foreignAccountService.getForeignAccountTransactions(exchangeTrading);

            long originalExchangingTotal = getOriginalExchangingTotal(
                    exchangeTrading.getStartingExchangeRate().getDealBaseRate(),
                    foreignAccountTransactions,
                    exchangeRate.getTargetCurrencyUnit()
            );
            long realExchangingTotal = getRealExchangingTotal(
                    foreignAccountTransactions,
                    exchangeRate.getTargetCurrencyUnit()
            );

            long additionalPoint = calculateAdditionalPoint(realExchangingTotal, originalExchangingTotal);

            PointWallet pointWallet = pointWalletService.getPointWallet(exchangeTrading.getUser());
            long pointToDeposit = exchangeTrading.flushCurrentAmount() + additionalPoint;
            pointWalletService.depositPoint(pointWallet, pointToDeposit);
            exchangeTrading.changeToComplete();

            sendSmsToUser(exchangeTrading);
            return;
        }

        int remainDays = exchangeTrading.getRemainDays(today);
        long exchangeAmount = exchangeTrading.getExchangeAmount(remainDays);

        ExchangeRateRangeStatistics last3MonthExchangeRateAverage =
                exchangeRateStatisticsService.getExchangeRateAverage(targetCurrency, THREE_MONTH);

        long weight = calculateExchangeWeight(last3MonthExchangeRateAverage.getExchangeRate(), exchangeRate);

        long targetCurrencyExchangeAmount = (long) ((exchangeAmount + weight) / exchangeRate.getDealBaseRate());
        exchangeService.exchange(exchangeTrading, targetCurrencyExchangeAmount);
    }

    @Override
    public boolean canApply(ExchangeTrading exchangeTrading) {
        return STABLE.equals(exchangeTrading.getStrategy());
    }

    private void sendSmsToUser(ExchangeTrading exchangeTrading) {
        User user = exchangeTrading.getUser();
        UserNotificationStatus userNotificationStatus = userNotificationStatusRepository.findByUser(user);
        if (userNotificationStatus.getStatus()) {
            smsService.sendOne(makeSmsMessage(exchangeTrading), exchangeTrading.getUser().getSmsPhoneNumber());
        }
    }

    private static long getRealExchangingTotal(List<ForeignAccountTransaction> foreignAccountTransactions,
                                               Long targetCurrencyUnit) {
        return foreignAccountTransactions.stream()
                .map(t -> (t.getAmount() / targetCurrencyUnit) * t.getTargetExchangeRate())
                .reduce(0.0, Double::sum).longValue();
    }

    private static long getOriginalExchangingTotal(double originalExchangeRate,
                                                   List<ForeignAccountTransaction> foreignAccountTransactions,
                                                   Long targetCurrencyUnit) {
        Long foreignCurrencyExchangeResultAmount = foreignAccountTransactions.stream()
                .map(ForeignAccountTransaction::getAmount)
                .reduce(0L, Long::sum);

        return (long) ((foreignCurrencyExchangeResultAmount / targetCurrencyUnit) * originalExchangeRate);
    }

    private static long calculateAdditionalPoint(long realExchangingTotal, long originalExchangingTotal) {
        if (realExchangingTotal > originalExchangingTotal) {
            return realExchangingTotal - originalExchangingTotal;
        }
        return 0L;
    }

    private static long calculateExchangeWeight(double exchangeRateAverage, ExchangeRate exchangeRate) {
        // TODO: 가중치에 대한 로직 고민
        final long weight = 100;
        double difference = exchangeRateAverage - exchangeRate.getDealBaseRate();
        return (long) (difference * weight);
    }

    private String makeSmsMessage(ExchangeTrading exchangeTrading) {
        return "[서포트립] " + exchangeTrading.getUser().getName() + "님의 '"
                + exchangeTrading.getDisplayName() + "' 여행을 위한 환전 거래가 완료되었어요!";
    }
}
