package com.supportrip.core.exchange.service;

import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.account.service.PointWalletService;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.supportrip.core.exchange.domain.TradingStrategy.TARGET;

@RequiredArgsConstructor
public class TargetExchangeStrategyService implements ExchangeStrategyService {
    private final ExchangeService exchangeService;
    private final PointWalletService pointWalletService;
    private final ExchangeRateService exchangeRateService;

    @Override
    public void execute(ExchangeTrading exchangeTrading, LocalDate today) {
        Currency targetCurrency = exchangeTrading.getTargetCurrency();
        ExchangeRate exchangeRate = exchangeRateService.getLatestExchangeRate(targetCurrency);

        if (exchangeTrading.isLastDate(today)) {
            exchangeAllAmount(exchangeTrading, exchangeRate);

            PointWallet pointWallet = pointWalletService.getPointWallet(exchangeTrading.getUser());
            pointWallet.increase(exchangeTrading.flushCurrentAmount());
            exchangeTrading.changeToComplete();
            return;
        }

        double targetExchangeRate = exchangeTrading.getTargetExchangeRate();
        if (targetExchangeRate >= exchangeRate.getDealBaseRate()) {
            exchangeAllAmount(exchangeTrading, exchangeRate);
        }
    }

    private void exchangeAllAmount(ExchangeTrading exchangeTrading, ExchangeRate exchangeRate) {
        long maxExchangeableAmount = exchangeTrading.getMaxExchangeableAmount(exchangeRate.getDealBaseRate());
        exchangeService.exchange(exchangeTrading, maxExchangeableAmount);
    }

    @Override
    public boolean canApply(ExchangeTrading exchangeTrading) {
        return TARGET.equals(exchangeTrading.getStrategy());
    }
}
