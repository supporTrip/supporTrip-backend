package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.LinkedAccount;
import com.supportrip.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkedAccountRepository extends JpaRepository<LinkedAccount, Long> {
    Optional<LinkedAccount> findByUser(User user);
}
