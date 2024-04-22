package com.supportrip.core.exchange.service;

import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.account.repository.ForeignAccountTransactionRepository;
import com.supportrip.core.account.service.ForeignAccountService;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExchangeService {
    private final ExchangeRateService exchangeRateService;
    private final ForeignAccountService foreignAccountService;
    private final ForeignAccountTransactionRepository foreignAccountTransactionRepository;

    @Transactional
    public void exchange(ExchangeTrading exchangeTrading, Currency toCurrency, Long toAmount) {
        User user = exchangeTrading.getUser();

        ExchangeRate latestExchangeRate = exchangeRateService.getLatestExchangeRate(toCurrency);
        long fromAmount = calculateAmountForExchange(toAmount, latestExchangeRate);

        exchangeTrading.reduceAmount(fromAmount);

        ForeignCurrencyWallet foreignCurrencyWallet = foreignAccountService.getForeignCurrencyWallet(user, toCurrency);
        foreignCurrencyWallet.deposit(toAmount);

        ForeignAccountTransaction foreignAccountTransaction = ForeignAccountTransaction.of(
                toAmount,
                latestExchangeRate.getDealBaseRate(),
                foreignCurrencyWallet.getTotalAmount(),
                foreignCurrencyWallet,
                exchangeTrading
        );

        foreignAccountTransactionRepository.save(foreignAccountTransaction);
    }

    private static long calculateAmountForExchange(Long toAmount, ExchangeRate latestExchangeRate) {
        return (long) ((toAmount / latestExchangeRate.getTargetCurrencyUnit()) * latestExchangeRate.getDealBaseRate());
    }
}
