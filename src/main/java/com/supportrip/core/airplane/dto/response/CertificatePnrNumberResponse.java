package com.supportrip.core.airplane.dto.response;

import com.supportrip.core.airplane.domain.AirplaneCertification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CertificatePnrNumberResponse {
    private final AirplaneCertification airplaneCertification;


}
