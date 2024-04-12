package com.supportrip.core.insurance.repository;

import com.supportrip.core.insurance.domain.InsuranceSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceSubscriptionRepository extends JpaRepository<InsuranceSubscription, Long> {

}
