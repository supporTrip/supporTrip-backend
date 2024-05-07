package com.supportrip.core.system.core.mydata.internal.application;

import com.supportrip.core.system.core.mydata.external.InsuranceFeignClient;
import com.supportrip.core.system.core.insurance.internal.presentation.response.InsuranceCorporationListResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.response.InsuranceListResponse;
import com.supportrip.core.system.core.insurance.internal.presentation.request.SendInsuranceRequest;
import com.supportrip.core.system.core.insurance.internal.presentation.response.SendInsuranceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsuranceClientService {
    private final InsuranceFeignClient insuranceFeignClient;

    public InsuranceListResponse getInsured(String token, String orgCode, String code) {
        return insuranceFeignClient.getInsured(token, orgCode, code);
    }

    public InsuranceCorporationListResponse getInsuredCorporation(String token) {
        return insuranceFeignClient.getInsuredCorporation(token);
    }

    public SendInsuranceResponse sendInsuredTransaction(String token, SendInsuranceRequest request){
        return insuranceFeignClient.sendInsuredTransaction(token, request);
    }
}
