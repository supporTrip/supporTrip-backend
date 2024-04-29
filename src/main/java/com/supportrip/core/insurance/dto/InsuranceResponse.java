package com.supportrip.core.insurance.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class InsuranceResponse {
    private String issueDate;
    private String corporationName;
    private String name;
    private String faceAmt;
    private String status;



}
