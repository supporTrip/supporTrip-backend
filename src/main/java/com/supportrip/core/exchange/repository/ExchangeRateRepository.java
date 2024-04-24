package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
