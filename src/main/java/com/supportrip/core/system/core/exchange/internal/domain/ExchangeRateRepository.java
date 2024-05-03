package com.supportrip.core.system.core.exchange.internal.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    @Query("SELECT er FROM ExchangeRate er WHERE er.targetCurrency = :targetCurrency ORDER BY er.date DESC LIMIT 1")
    Optional<ExchangeRate> findLatestExchange(@Param("targetCurrency") Currency targetCurrency);

    List<ExchangeRate> findByTargetCurrencyAndCreatedAtBetween(Currency targetCurrency, LocalDateTime startedAt, LocalDateTime endedAt);
}
