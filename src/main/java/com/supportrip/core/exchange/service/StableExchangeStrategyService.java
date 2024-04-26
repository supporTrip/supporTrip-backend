package com.supportrip.core.exchange.service;

import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.account.service.ForeignAccountService;
import com.supportrip.core.account.service.PointWalletService;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeRateRangeStatistics;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.supportrip.core.exchange.domain.TradingStrategy.STABLE;

@RequiredArgsConstructor
public class StableExchangeStrategyService implements ExchangeStrategyService {
    private final ExchangeService exchangeService;
    private final ExchangeRateStatisticsService exchangeRateStatisticsService;
    private final ExchangeRateService exchangeRateService;
    private final PointWalletService pointWalletService;
    private final ForeignAccountService foreignAccountService;

    @Override
    public void execute(ExchangeTrading exchangeTrading, LocalDate today) {
        Currency targetCurrency = exchangeTrading.getTargetCurrency();
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate(targetCurrency);

        if (exchangeTrading.isLastDate(today)) {
            long maxExchangeableAmount = exchangeTrading.getMaxExchangeableAmount(exchangeRate.getDealBaseRate());
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
            pointWallet.increase(exchangeTrading.flushCurrentAmount() + additionalPoint);
            exchangeTrading.changeToComplete();
            return;
        }

        int remainDays = exchangeTrading.getRemainDays(today);
        long exchangeAmount = exchangeTrading.getExchangeAmount(remainDays);

        ExchangeRateRangeStatistics last3MonthExchangeRateAverage =
                exchangeRateStatisticsService.getLast3MonthExchangeRateAverage(targetCurrency);

        long weight = calculateExchangeWeight(last3MonthExchangeRateAverage.getAverageRate(), exchangeRate);

        exchangeService.exchange(exchangeTrading, exchangeAmount + weight);
    }

    @Override
    public boolean canApply(ExchangeTrading exchangeTrading) {
        return STABLE.equals(exchangeTrading.getStrategy());
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
}
