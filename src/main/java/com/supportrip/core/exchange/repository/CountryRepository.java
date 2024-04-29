package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.Country;
import com.supportrip.core.exchange.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByCurrency(Currency currency);
    Country findByName(String name);
}
