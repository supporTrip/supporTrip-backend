package com.supportrip.core.account.repository;

import com.supportrip.core.account.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Bank findByName(String name);

    Optional<Bank> findByCode(String code);
}
