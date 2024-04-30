package com.supportrip.core.insurance.service;

import com.supportrip.core.feign.service.InsuranceClientService;
import com.supportrip.core.insurance.domain.FlightInsurance;
import com.supportrip.core.insurance.domain.InsuranceCompany;
import com.supportrip.core.insurance.dto.*;
import com.supportrip.core.insurance.repository.FlightInsuranceRepository;
import com.supportrip.core.insurance.repository.InsuranceCompanyRepository;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserCI;
import com.supportrip.core.user.repository.UserCIRepository;
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
        
        for(InsuranceCorporationResponse insuranceCorporationResponse : corporations){
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

    public RecomandInsuranceListResponse getRecomandInsuranceList(User user, LocalDateTime departAt, LocalDateTime arrivalAt){
        UserCI userCI = userCIRepository.findByUser(user);
        List<InsuranceCorporationResponse> corporations = getCorporations(userCI);
        List<RecomandInsuranceResponse> recomandInsuranceResponseList = new ArrayList<>();
        int age = flightInsuranceService.calculateAge(user.getBirthDay());
        int period = flightInsuranceService.calculatePeriod(departAt, arrivalAt);
        List<FlightInsurance> calInsus = new ArrayList<>();
        if(corporations.isEmpty()) {
            List<FlightInsurance> insurances = flightInsuranceRepository.findTop3ByOrderByPremiumAsc();
            calInsus = flightInsuranceCalculatePremiumService.calculatePremium(age,period, "standard", user.getGender(),insurances);
            for(FlightInsurance insurance : calInsus)
                recomandInsuranceResponseList.add(RecomandInsuranceResponse.of(insurance.getInsuranceCompany().getName(), insurance.getName(), insurance.getPremium()));

        }else {
            for(InsuranceCorporationResponse insuranceCorporationResponse : corporations){
                List<InsuranceResponse> insurances = insuranceClientService.getInsured(userCI.getToken(), insuranceCorporationResponse.getOrg_code(), "14").getInsuranceList();
                List<FlightInsurance> insuranceList = new ArrayList<>();
                    for(InsuranceResponse insuranceResponse : insurances){
                        InsuranceCompany insuranceCompany = insuranceCompanyRepository.findByName(insuranceResponse.getCorporationName());
                        insuranceList.add(flightInsuranceRepository.findByInsuranceCompany(insuranceCompany));
                    }
                calInsus = flightInsuranceCalculatePremiumService.calculatePremium(age,period, "standard", user.getGender(),insuranceList);

            }
        }

        for(FlightInsurance insurance : calInsus)
            recomandInsuranceResponseList.add(RecomandInsuranceResponse.of(insurance.getInsuranceCompany().getName(), insurance.getName(), insurance.getPremium()));
        return RecomandInsuranceListResponse.of(recomandInsuranceResponseList);
    }

    public List<InsuranceCorporationResponse> getCorporations(UserCI userCI){
        InsuranceCorporationListResponse corporationList = insuranceClientService.getInsuredCorporation(userCI.getToken());

        return corporationList.getCorporationList();
    }
}
