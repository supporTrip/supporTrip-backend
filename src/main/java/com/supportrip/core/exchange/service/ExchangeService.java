package com.supportrip.core.exchange.service;

import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.account.repository.ForeignAccountTransactionRepository;
import com.supportrip.core.account.service.ForeignAccountService;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.exchange.dto.response.ExchangeTransactionListResponse;
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
    public void exchange(ExchangeTrading exchangeTrading, Long amount) {
        Currency targetCurrency = exchangeTrading.getTargetCurrency();
        User user = exchangeTrading.getUser();

        ExchangeRate latestExchangeRate = exchangeRateService.getLatestExchangeRate(targetCurrency);
        long fromAmount = calculateAmountForExchange(amount, latestExchangeRate);

        exchangeTrading.reduceAmount(fromAmount);

        ForeignCurrencyWallet foreignCurrencyWallet = foreignAccountService.getForeignCurrencyWallet(user, targetCurrency);
        foreignCurrencyWallet.deposit(amount);

        ForeignAccountTransaction foreignAccountTransaction = ForeignAccountTransaction.of(
                amount,
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
