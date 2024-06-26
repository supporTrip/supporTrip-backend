package com.supportrip.core.system.core.insurance.internal.application;

import com.supportrip.core.system.core.insurance.internal.domain.FlightInsurance;
import com.supportrip.core.system.core.insurance.internal.domain.InsuranceCompany;
import com.supportrip.core.system.core.insurance.internal.application.FlightInsuranceCalculatePremiumService;
import com.supportrip.core.system.core.user.internal.domain.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightInsuranceCalculatePremiumServiceTest {

    FlightInsuranceCalculatePremiumService calculatePremiumService = new FlightInsuranceCalculatePremiumService();

    @Test
    @DisplayName("보험료 계산")
    void calculatePremium() {
        //given
        int period = 4;
        int age = 20;
        Gender gender = Gender.FEMALE;
        String planName = "standard";

        InsuranceCompany insuranceCompany = InsuranceCompany.from("한화생명");
        FlightInsurance flightInsurance = FlightInsurance.of(insuranceCompany, "한화생명 해외여행자 보험", 1000, 15, 60, true, true, true);

        //when
        int premium = calculatePremiumService.calculatePremium(age, period, planName, gender, flightInsurance);

        //then
        assertEquals(5190, premium);
    }
}