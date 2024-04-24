package com.supportrip.core.user.repository;

import com.supportrip.core.user.domain.PhoneVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneVerificationRepository extends JpaRepository<PhoneVerification, Long> {
}
