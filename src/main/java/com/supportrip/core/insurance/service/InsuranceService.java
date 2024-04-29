package com.supportrip.core.insurance.service;

import com.supportrip.core.feign.service.InsuranceClientService;
import com.supportrip.core.insurance.dto.InsuranceCorporationListResponse;
import com.supportrip.core.insurance.dto.InsuranceCorporationResponse;
import com.supportrip.core.insurance.dto.InsuranceListResponse;
import com.supportrip.core.insurance.dto.InsuranceResponse;
import com.supportrip.core.user.domain.User;
import com.supportrip.core.user.domain.UserCI;
import com.supportrip.core.user.repository.UserCIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final UserCIRepository userCIRepository;
    private final InsuranceClientService insuranceClientService;
    public InsuranceListResponse getInsuranceList(User user) {
        UserCI userCI = userCIRepository.findByUser(user);


        InsuranceCorporationListResponse corporationList = insuranceClientService.getInsuredCorporation(userCI.getToken());

        List<InsuranceCorporationResponse> corporations = corporationList.getCorporationList();

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
}
