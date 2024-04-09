package com.supportrip.core.insurance.repository;

import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.SpecialContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightInsuranceRepository extends JpaRepository<FlightInsurance, Long> {

    /**
     * 나이, 플랜 보험상품 필터 조회
     */
    @Query("SELECT f FROM FlightInsurance f JOIN fetch f.insuranceCompany WHERE " +
            ":age BETWEEN f.minJoinAge AND f.maxJoinAge AND " +
            "f.planName = :planName")
    List<FlightInsurance> findByAgeAndPlan (@Param("age") int age,
                                            @Param("planName") String planName);


    @Query("SELECT s FROM SpecialContract s WHERE s.id = :id")
    List<SpecialContract> findByIdThreeContract(@Param("id") Long id);
}
