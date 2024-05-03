package com.supportrip.core.system.core.account.internal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByPointWalletOrderByCreatedAtDesc(PointWallet pointWallet);
}
