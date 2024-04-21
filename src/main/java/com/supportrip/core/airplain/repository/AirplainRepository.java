package com.supportrip.core.airplain.repository;

import com.supportrip.core.airplain.domain.AirplainCertification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirplainRepository extends JpaRepository<AirplainCertification, Long> {
}
