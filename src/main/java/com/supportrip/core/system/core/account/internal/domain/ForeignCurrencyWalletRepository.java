package com.supportrip.core.system.core.account.internal.domain;

import com.supportrip.core.system.core.exchange.internal.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ForeignCurrencyWalletRepository extends JpaRepository<ForeignCurrencyWallet, Long> {
    List<ForeignCurrencyWallet> findByForeignAccountAndTotalAmountGreaterThan(ForeignAccount foreignAccount, Long amount);

    List<ForeignCurrencyWallet> findByForeignAccount(ForeignAccount foreignAccount);

    Optional<ForeignCurrencyWallet> findByForeignAccountAndCurrency(ForeignAccount foreignAccount, Currency currency);
}
