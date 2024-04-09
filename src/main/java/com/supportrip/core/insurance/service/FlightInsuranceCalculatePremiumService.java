package com.supportrip.core.insurance.service;

import com.supportrip.core.insurance.domain.FlightInsurance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightInsuranceCalculatePremiumService {

    /**
     * 여행기간, 성별, 플랜마다 보험료 책정
     */
    public List<FlightInsurance> calculatePremium(int period, String gender, List<FlightInsurance> filteredInsurances) {
        List<FlightInsurance> newInsurancePremium = new ArrayList<>();

        for (FlightInsurance flightInsurance : filteredInsurances) {
            int premium = flightInsurance.getPremium();
            if (flightInsurance.getPlanName().equals("standard")) {
                premium *= 2;
            } else if (flightInsurance.getPlanName().equals("advanced")) {
                premium *= 3;
            }

            if (gender.equals("female")) {
                premium += 750;
            }
            premium += period * 860;

            flightInsurance.setPremium(premium);
            newInsurancePremium.add(flightInsurance);
        }

        return filteredInsurances;
    }
}
