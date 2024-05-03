package com.supportrip.core.system.core.insurance.internal.domain;

import com.supportrip.core.system.core.insurance.internal.domain.SpecialContract;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialContractRepository extends JpaRepository<SpecialContract, Long> {
    List<SpecialContract> findByFlightInsuranceId(Long id, Pageable pageable);

    List<SpecialContract> findByFlightInsuranceId(Long flightInsuranceId);
}
