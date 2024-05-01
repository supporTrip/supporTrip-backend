package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.ForeignAccount;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.exchange.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ForeignCurrencyWalletRepository extends JpaRepository<ForeignCurrencyWallet, Long> {
    List<ForeignCurrencyWallet> findByForeignAccountAndTotalAmountGreaterThan(ForeignAccount foreignAccount, Long amount);

    List<ForeignCurrencyWallet> findByForeignAccount(ForeignAccount foreignAccount);

    Optional<ForeignCurrencyWallet> findByForeignAccountAndCurrency(ForeignAccount foreignAccount, Currency currency);
}
