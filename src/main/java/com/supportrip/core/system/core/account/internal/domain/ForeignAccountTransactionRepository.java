package com.supportrip.core.system.core.account.internal.domain;

import com.supportrip.core.system.core.exchange.internal.domain.ExchangeTrading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForeignAccountTransactionRepository extends JpaRepository<ForeignAccountTransaction, Long> {
    List<ForeignAccountTransaction> findByForeignCurrencyWalletOrderByCreatedAtDesc(ForeignCurrencyWallet foreignCurrencyWallet);

    List<ForeignAccountTransaction> findByExchangeTrading(ExchangeTrading exchangeTrading);
}
