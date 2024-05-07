package com.supportrip.core.system.core.account.internal.application;

import com.supportrip.core.system.core.account.internal.domain.ForeignAccount;
import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransaction;
import com.supportrip.core.system.core.account.internal.domain.ForeignCurrencyWallet;
import com.supportrip.core.context.error.exception.notfound.ForeignAccountNotFoundException;
import com.supportrip.core.system.core.account.internal.domain.ForeignCurrencyWalletRepository;
import com.supportrip.core.system.core.account.internal.domain.ForeignAccountRepository;
import com.supportrip.core.system.core.account.internal.domain.ForeignAccountTransactionRepository;
import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import com.supportrip.core.system.core.user.internal.domain.User;
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
