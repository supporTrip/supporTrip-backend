package com.supportrip.core.system.core.insurance.internal.domain;

import com.supportrip.core.system.core.insurance.internal.domain.InsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long> {
    InsuranceCompany findByName(String name);

}
