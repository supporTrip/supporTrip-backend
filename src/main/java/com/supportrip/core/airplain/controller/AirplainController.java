package com.supportrip.core.airplain.controller;

import com.supportrip.core.airplain.domain.AirplainCertification;
import com.supportrip.core.airplain.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplain.service.AirplainService;
import com.supportrip.core.common.SimpleIdResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/airplain")
public class AirplainController {
    private final AirplainService airplainService;

    @PostMapping("/certification")
    public SimpleIdResponse certificatePnrNumber(@RequestBody CertificatePnrNumberRequest certificatePnrNumberRequest){

        AirplainCertification airplainCertification = airplainService.certificatePnrNumber(certificatePnrNumberRequest);

        return SimpleIdResponse.from(airplainCertification.getId());
    }

}
