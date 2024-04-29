package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.PointTransaction;
import com.supportrip.core.account.domain.PointWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByPointWalletOrderByCreatedAtDesc(PointWallet pointWallet);
}
