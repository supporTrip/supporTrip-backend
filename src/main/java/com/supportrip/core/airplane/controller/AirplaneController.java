package com.supportrip.core.airplane.controller;

import com.supportrip.core.airplane.domain.AirplaneCertification;
import com.supportrip.core.airplane.dto.request.CertificatePnrNumberRequest;
import com.supportrip.core.airplane.dto.response.CertificatePnrNumberResponse;
import com.supportrip.core.airplane.service.AirplaneService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/airplain")
public class AirplaneController {
    private final AirplaneService airplaneService;

    @PostMapping("/certification")
    public CertificatePnrNumberResponse certificatePnrNumber(@RequestBody CertificatePnrNumberRequest certificatePnrNumberRequest){
        return airplaneService.certificatePnrNumber(certificatePnrNumberRequest);
    }

}
