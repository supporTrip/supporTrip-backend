package com.supportrip.core.account.service;

import com.supportrip.core.account.domain.ForeignAccount;
import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.account.exception.ForeignAccountNotFoundException;
import com.supportrip.core.account.repository.ForeignAccountRepository;
import com.supportrip.core.account.repository.ForeignAccountTransactionRepository;
import com.supportrip.core.account.repository.ForeignCurrencyWalletRepository;
import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import com.supportrip.core.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForeignAccountService {
    private final ForeignAccountRepository foreignAccountRepository;
    private final ForeignCurrencyWalletRepository foreignCurrencyWalletRepository;
    private final ForeignAccountTransactionRepository foreignAccountTransactionRepository;

    public ForeignAccount getForeignAccount(User user) {
        return foreignAccountRepository.findByUser(user).orElseThrow(ForeignAccountNotFoundException::new);
    }

    public List<ForeignCurrencyWallet> getForeignCurrencyWallets(User user) {
        ForeignAccount foreignAccount = getForeignAccount(user);
        return foreignCurrencyWalletRepository.findByForeignAccount(foreignAccount);
    }

    public ForeignCurrencyWallet getForeignCurrencyWallet(User user, Currency currency) {
        ForeignAccount foreignAccount = getForeignAccount(user);
        return foreignCurrencyWalletRepository.findByForeignAccountAndCurrency(foreignAccount, currency).get();
    }

    public List<ForeignAccountTransaction> getForeignAccountTransactions(ExchangeTrading exchangeTrading) {
        return foreignAccountTransactionRepository.findByExchangeTrading(exchangeTrading);
    }
}
