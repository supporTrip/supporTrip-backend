package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.PointWallet;
import com.supportrip.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointWalletRepository extends JpaRepository<PointWallet, Long> {
    Optional<PointWallet> findByUser(User user);
}
