package com.supportrip.core.user.dto.response;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCardApproval {
    private Long id;
    private String status;
    private Double approvedAmt;
    private String countryCode;
    private String currencyCode;
    private LocalDateTime approvedAt;
}
