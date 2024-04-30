package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);
    List<Currency> findByCodeNot(String code);
}
