package com.supportrip.core.system.core.insurance.internal.application;

import com.supportrip.core.system.core.insurance.internal.domain.FlightInsurance;
import com.supportrip.core.system.core.insurance.internal.domain.FlightInsuranceRepository;
import com.supportrip.core.system.core.insurance.internal.domain.InsuranceCompany;
import com.supportrip.core.system.core.insurance.internal.domain.InsuranceCompanyRepository;
import com.supportrip.core.system.core.insurance.internal.presentation.response.*;
import com.supportrip.core.system.core.mydata.internal.application.InsuranceClientService;
import com.supportrip.core.system.core.user.internal.domain.User;
import com.supportrip.core.system.core.user.internal.domain.UserCI;
import com.supportrip.core.system.core.user.internal.domain.UserCIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final UserCIRepository userCIRepository;
    private final InsuranceClientService insuranceClientService;
    private final FlightInsuranceRepository flightInsuranceRepository;
    private final InsuranceCompanyRepository insuranceCompanyRepository;
    private final FlightInsuranceCalculatePremiumService flightInsuranceCalculatePremiumService;
    private final FlightInsuranceService flightInsuranceService;

    public InsuranceListResponse getInsuranceList(User user) {
        UserCI userCI = userCIRepository.findByUser(user);
        List<InsuranceCorporationResponse> corporations = getCorporations(userCI);

        List<InsuranceResponse> insuranceResponses = new ArrayList<>();

        for (InsuranceCorporationResponse insuranceCorporationResponse : corporations) {
            List<InsuranceResponse> insurances = insuranceClientService.getInsured(userCI.getToken(), insuranceCorporationResponse.getOrg_code(), "14").getInsuranceList();
            insuranceResponses.addAll(insurances);
        }

        Collections.sort(insuranceResponses, new Comparator<InsuranceResponse>() {
            @Override
            public int compare(InsuranceResponse o1, InsuranceResponse o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
                try {
                    Date date1 = format.parse(o1.getIssueDate());
                    Date date2 = format.parse(o2.getIssueDate());
                    return date2.compareTo(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });


        return InsuranceListResponse.of(insuranceResponses);
    }

    public RecomandInsuranceListResponse getRecomandInsuranceList(User user, LocalDateTime departAt, LocalDateTime arrivalAt) {
        UserCI userCI = userCIRepository.findByUser(user);
        List<InsuranceCorporationResponse> corporations = getCorporations(userCI);
        List<RecomandInsuranceResponse> recomandInsuranceResponseList = new ArrayList<>();
        int age = flightInsuranceService.calculateAge(user.getBirthDay());
        int period = flightInsuranceService.calculatePeriod(departAt, arrivalAt);
        List<FilterAndCalPremiumResponse> calInsus = new ArrayList<>();

        // 보험 추천할 리스트 가져오기
        List<FlightInsurance> insurances = getInsurances(corporations, userCI.getToken());

        // 입력된 정보에 맞게 보험금 계산
        calInsus = flightInsuranceService.calculatedInsurances(age, period, "standard", user.getGender(), departAt, arrivalAt, insurances);

        for (FilterAndCalPremiumResponse insurance : calInsus) {
            recomandInsuranceResponseList.add(RecomandInsuranceResponse.of(insurance.getCompanyName(), insurance.getInsuranceName(), insurance.getPremium()));
        }
        return RecomandInsuranceListResponse.of(recomandInsuranceResponseList);
    }

    public List<FlightInsurance> getInsurances(List<InsuranceCorporationResponse> corporations, String token) {
        List<FlightInsurance> insurances = new ArrayList<>();
        if (corporations.isEmpty()) {
            insurances = flightInsuranceRepository.findTop3ByOrderByPremiumAsc();
        } else {
            for (InsuranceCorporationResponse insuranceCorporationResponse : corporations) {
                List<InsuranceResponse> insurancesFromAPI = insuranceClientService.getInsured(token, insuranceCorporationResponse.getOrg_code(), "14").getInsuranceList();
                for (InsuranceResponse insuranceResponse : insurancesFromAPI) {
                    InsuranceCompany insuranceCompany = insuranceCompanyRepository.findByName(insuranceResponse.getCorporationName());
                    insurances.add(flightInsuranceRepository.findByInsuranceCompany(insuranceCompany));
                }
            }
        }

        if (insurances.size() >= 3) {
            insurances = insurances.subList(0, 3);
        } else {
            List<FlightInsurance> otherInsurances = flightInsuranceRepository.findTop3ByOrderByPremiumAsc();
            for (FlightInsurance insurance : insurances) {
                otherInsurances.remove(insurance);
            }
            insurances.addAll(otherInsurances.subList(0, Math.min(3 - insurances.size(), otherInsurances.size())));
        }

        return insurances;
    }

    public List<InsuranceCorporationResponse> getCorporations(UserCI userCI) {
        InsuranceCorporationListResponse corporationList = insuranceClientService.getInsuredCorporation(userCI.getToken());

        return corporationList.getCorporationList();
    }
}
