package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.LinkedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedAccountRepository extends JpaRepository<LinkedAccount, Long> {
}
