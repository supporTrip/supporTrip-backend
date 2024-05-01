package com.supportrip.core.feign.controller;

import com.supportrip.core.insurance.dto.InsuranceCorporationListResponse;
import com.supportrip.core.insurance.dto.InsuranceListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "insurance-service", url = "${mydata.api.insu}")
public interface InsuranceFeignClient {

    @GetMapping("insured/basic")
    InsuranceListResponse getInsured(@RequestParam(value = "token") String token,
                                     @RequestParam(value = "org_code") String org_code,
                                     @RequestParam(value = "code") String code);

    @GetMapping("insured")
    InsuranceCorporationListResponse getInsuredCorporation(@RequestParam(value = "token") String token);
}