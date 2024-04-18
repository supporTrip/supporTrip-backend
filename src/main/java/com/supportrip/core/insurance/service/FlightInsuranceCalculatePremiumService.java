package com.supportrip.core.insurance.service;

import com.supportrip.core.insurance.domain.FlightInsurance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightInsuranceCalculatePremiumService {

    /**
     * 여행기간, 성별, 플랜마다 보험료 책정
     */
    public List<FlightInsurance> calculatePremium(int age, int period, String planName, String gender, List<FlightInsurance> filteredInsurances) {
        for (FlightInsurance flightInsurance : filteredInsurances) {
            int premium = flightInsurance.getPremium();

            if (age < 15) {
                premium *= 0.7;
            } else if (age >= 70) {
                premium *= 1.5;
            }

            if (planName.equals("advanced")) {
                premium *= 1.5;
            }

            if (gender.equals("female")) {
                premium += 750;
            }
            premium += period * 860;

            flightInsurance.setPremium(premium);
        }

        return filteredInsurances;
    }
}
