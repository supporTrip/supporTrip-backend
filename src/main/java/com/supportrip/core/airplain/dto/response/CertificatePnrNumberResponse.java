package com.supportrip.core.airplain.dto.response;

import com.supportrip.core.airplain.domain.AirplainCertification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CertificatePnrNumberResponse {
    private final AirplainCertification airplainCertification;


}
