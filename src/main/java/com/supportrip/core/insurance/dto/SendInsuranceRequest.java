package com.supportrip.core.insurance.dto;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SendInsuranceRequest {
    private final String corporationName;
    private final String name;
    private final Long faceAmt;
    private final LocalDate issueDate;

    @Builder(access = AccessLevel.PRIVATE)

    public SendInsuranceRequest(String corporationName, String name, Long faceAmt, LocalDate issueDate) {
        this.corporationName = corporationName;
        this.name = name;
        this.faceAmt = faceAmt;
        this.issueDate = issueDate;
    }

    public static SendInsuranceRequest of(String corporationName, String name, Long faceAmt, LocalDate issueDate){
        return SendInsuranceRequest.builder()
                .corporationName(corporationName)
                .name(name)
                .faceAmt(faceAmt)
                .issueDate(issueDate)
                .build();
    }
}
