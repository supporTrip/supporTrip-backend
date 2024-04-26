package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import com.supportrip.core.exchange.domain.ExchangeTrading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForeignAccountTransactionRepository extends JpaRepository<ForeignAccountTransaction, Long> {
    List<ForeignAccountTransaction> findByForeignCurrencyWalletOrderByCreatedAtDesc(ForeignCurrencyWallet foreignCurrencyWallet);

    List<ForeignAccountTransaction> findByExchangeTrading(ExchangeTrading exchangeTrading);
}
