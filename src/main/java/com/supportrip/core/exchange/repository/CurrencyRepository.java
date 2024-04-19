package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}
