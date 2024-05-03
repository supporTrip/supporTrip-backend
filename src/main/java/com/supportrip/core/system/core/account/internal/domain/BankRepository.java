package com.supportrip.core.system.core.account.internal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Long> {
    Bank findByName(String name);

    Optional<Bank> findByCode(String code);
}
