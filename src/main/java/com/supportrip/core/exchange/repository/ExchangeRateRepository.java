package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.Currency;
import com.supportrip.core.exchange.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    Optional<ExchangeRate> findLatestExchangeByTargetCurrency(Currency targetCurrency);

    List<ExchangeRate> findByTargetCurrencyAndCreatedAtBetween(Currency targetCurrency, LocalDateTime startedAt, LocalDateTime endedAt);
}
