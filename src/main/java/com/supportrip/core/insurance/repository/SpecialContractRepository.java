package com.supportrip.core.insurance.repository;

import com.supportrip.core.insurance.domain.SpecialContract;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialContractRepository extends JpaRepository<SpecialContract, Long> {

    List<SpecialContract> findByFlightInsuranceId(Long id, Pageable pageable);
}
