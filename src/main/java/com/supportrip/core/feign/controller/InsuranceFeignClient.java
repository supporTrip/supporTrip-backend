package com.supportrip.core.feign.controller;

import com.supportrip.core.insurance.dto.InsuranceCorporationListResponse;
import com.supportrip.core.insurance.dto.InsuranceListResponse;
import com.supportrip.core.insurance.dto.SendInsuranceRequest;
import com.supportrip.core.insurance.dto.SendInsuranceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "insurance-service", url = "${mydata.api.insu}")
public interface InsuranceFeignClient {

    @GetMapping("insured/basic")
    InsuranceListResponse getInsured(@RequestParam(value = "token") String token,
                                     @RequestParam(value = "org_code") String org_code,
                                     @RequestParam(value = "code") String code);

    @GetMapping("insured")
    InsuranceCorporationListResponse getInsuredCorporation(@RequestParam(value = "token") String token);

    @PostMapping("insured")
    SendInsuranceResponse sendInsuredTransaction(@RequestParam(value = "token")String token, @RequestBody SendInsuranceRequest request);
}