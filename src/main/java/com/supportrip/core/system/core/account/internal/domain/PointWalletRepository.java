package com.supportrip.core.system.core.account.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointWalletRepository extends JpaRepository<PointWallet, Long> {
    Optional<PointWallet> findByUser(User user);
}
