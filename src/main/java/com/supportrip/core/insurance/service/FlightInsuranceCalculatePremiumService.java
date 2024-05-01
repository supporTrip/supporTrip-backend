package com.supportrip.core.insurance.service;

import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.user.domain.Gender;
import org.springframework.stereotype.Service;

@Service
public class FlightInsuranceCalculatePremiumService {

    /**
     * 여행기간, 성별, 플랜마다 보험료 책정
     */
    public int calculatePremium(int age, int period, String planName, Gender gender, FlightInsurance flightInsurance) {

        int calPremium = flightInsurance.getPremium();

        if (age < 15) {
            calPremium *= 0.7;
        } else if (age >= 70) {
            calPremium *= 1.5;
        }

        if (planName.equals("advanced")) {
            calPremium *= 1.5;
        }

        if (gender == Gender.FEMALE) {
            calPremium += 750;
        }
        calPremium += period * 860;


        return calPremium;
    }
}
