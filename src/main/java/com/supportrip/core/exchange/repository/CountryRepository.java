package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.Country;
import com.supportrip.core.exchange.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByCurrency(Currency currency);
    Country findByName(String name);
    List<Country> findByNameNot(String name);
}
