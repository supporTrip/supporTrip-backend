package com.supportrip.core.insurance.service;

import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.InsuranceCompany;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FlightInsuranceCalculatePremiumServiceTest {

    @Autowired
    FlightInsuranceCalculatePremiumService calculatePremiumService;

    @Test
    @DisplayName("보험료 계산")
    void calculatePremium() {
        //given
        int period = 4;
        int age = 20;
        String gender = "female";

        InsuranceCompany insuranceCompany = InsuranceCompany.from("한화생명");
        List<FlightInsurance> flightInsurances = Arrays.asList(
                FlightInsurance.of(insuranceCompany, "한화생명 해외여행자 보험", 1000, 15, 60, "standard", true, true, true, true, true)

        );

        //when
        List<FlightInsurance> result = calculatePremiumService.calculatePremium(age, period, gender, flightInsurances);

        //then
        assertEquals(5190, result.get(0).getPremium());
    }
}