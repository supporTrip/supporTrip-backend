package com.supportrip.core.system.core.exchange.internal.domain;

import com.supportrip.core.system.core.user.internal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeTradingRepository extends JpaRepository<ExchangeTrading, Long> {
    List<ExchangeTrading> findByUserAndStatus(User user, TradingStatus status);
    List<ExchangeTrading> findByStatus(TradingStatus status);

    List<ExchangeTrading> findByUserOrderByCreatedAtDesc(User user);
}
