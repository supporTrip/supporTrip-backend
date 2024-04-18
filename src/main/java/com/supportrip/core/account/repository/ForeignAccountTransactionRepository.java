package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.ForeignAccountTransaction;
import com.supportrip.core.account.domain.ForeignCurrencyWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForeignAccountTransactionRepository extends JpaRepository<ForeignAccountTransaction, Long> {
    List<ForeignAccountTransaction> findByForeignCurrencyWalletOrderByCreatedAtDesc(ForeignCurrencyWallet foreignCurrencyWallet);
}
