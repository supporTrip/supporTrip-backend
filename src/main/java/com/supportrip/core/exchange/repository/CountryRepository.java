package com.supportrip.core.exchange.repository;

import com.supportrip.core.exchange.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByName(String name);
}
