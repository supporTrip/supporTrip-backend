package com.supportrip.core.airplain.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CertificatePnrNumberRequest {
    private String country;
    private String pnrNumber;
    private LocalDateTime departAt;
}
