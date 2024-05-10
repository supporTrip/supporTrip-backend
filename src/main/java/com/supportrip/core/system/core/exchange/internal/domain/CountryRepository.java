package com.supportrip.core.system.core.exchange.internal.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByCurrency(Currency currency);
    Country findByName(String name);
    List<Country> findByNameNot(String name);
    Country findByCode(String code);
}
