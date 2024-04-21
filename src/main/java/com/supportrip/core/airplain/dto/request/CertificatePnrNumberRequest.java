package com.supportrip.core.airplain.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CertificatePnrNumberRequest {
    private final String country;
    private final String pnrNumber;
    private final LocalDateTime departAt;
}
