package com.supportrip.core.user.repository;

import com.supportrip.core.user.domain.UserConsentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConsentStatusRepository extends JpaRepository<UserConsentStatus, Long> {
}
