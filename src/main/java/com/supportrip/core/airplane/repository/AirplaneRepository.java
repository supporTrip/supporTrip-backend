package com.supportrip.core.airplane.repository;

import com.supportrip.core.airplane.domain.AirplaneCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirplaneRepository extends JpaRepository<AirplaneCertification, Long> {
}
