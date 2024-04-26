package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT er FROM ExchangeRate er WHERE er.targetCurrency = :targetCurrency ORDER BY er.date DESC LIMIT 1")
    Optional<ExchangeRate> findLatestExchange(Currency targetCurrency);

    List<ExchangeRate> findByTargetCurrencyAndCreatedAtBetween(Currency targetCurrency, LocalDateTime startedAt, LocalDateTime endedAt);
}
