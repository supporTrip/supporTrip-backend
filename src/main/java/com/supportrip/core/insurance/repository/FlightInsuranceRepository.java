package com.supportrip.core.insurance.repository;

import com.supportrip.core.insurance.domain.TravelInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TravelInsuranceRepository extends JpaRepository<TravelInsurance, Long> {

    @Query(value = )
    TravelInsurance findByCoverageAtAndBirthdayAndPlanAndCategory(LocalDateTime coverageStartAt, //20240413
                                                                  LocalDateTime coverageEndAt, //202404
                                                                  String birthDay, String plan, //
                                                                  boolean overseasMedicalExpenses,
                                                                  boolean phoneLoss,
                                                                  boolean flightDelay,
                                                                  boolean passportLoss,
                                                                  boolean foodPoisoning);
}
