package com.supportrip.core.system.core.insurance.internal.presentation.request;

import com.supportrip.core.system.core.user.internal.domain.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SearchFlightInsuranceRequest {
    @NotNull(message = "출발시간을 입력해주세요")
    private LocalDateTime departAt;

    @NotNull(message = "도착시간을 입력해주세요")
    private LocalDateTime arrivalAt;

    @NotNull(message = "생년월일 8자리를 입력해주세요.")
    private LocalDate birthDay;

    @NotNull(message = "성별을 입력해주세요.")
    private Gender gender;

    private String planName;
    private Boolean flightDelay;
    private Boolean passportLoss;
    private Boolean foodPoisoning;

    @ConstructorProperties({"departAt", "arrivalAt", "birthDay", "gender", "planName", "flightDelay", "passportLoss", "foodPoisoning"})
    @Builder(access = AccessLevel.PRIVATE)
    public SearchFlightInsuranceRequest(LocalDateTime departAt, LocalDateTime arrivalAt, LocalDate birthDay, Gender gender, String planName, Boolean flightDelay, Boolean passportLoss, Boolean foodPoisoning) {
        this.departAt = departAt;
        this.arrivalAt = arrivalAt;
        this.birthDay = birthDay;
        this.gender = gender;
        this.planName = planName.equals("") ? "standard" : planName;
        this.flightDelay = flightDelay == null ? Boolean.FALSE : flightDelay;
        this.passportLoss = passportLoss == null ? Boolean.FALSE : passportLoss;
        this.foodPoisoning = foodPoisoning == null ? Boolean.FALSE : foodPoisoning;
    }

    public static SearchFlightInsuranceRequest of(LocalDateTime departAt, LocalDateTime arrivalAt, LocalDate birthDay, Gender gender, String planName, Boolean flightDelay, Boolean passportLoss, Boolean foodPoisoning) {
        return builder()
                .departAt(departAt)
                .arrivalAt(arrivalAt)
                .birthDay(birthDay)
                .gender(gender)
                .planName(planName)
                .flightDelay(flightDelay)
                .passportLoss(passportLoss)
                .foodPoisoning(foodPoisoning)
                .build();
    }

    @Override
    public String toString() {
        return "{" +
                "departAt=" + departAt +
                ", arrivalAt=" + arrivalAt +
                ", birthDay=" + birthDay +
                ", gender=" + gender +
                ", planName='" + planName + '\'' +
                ", flightDelay=" + flightDelay +
                ", passportLoss=" + passportLoss +
                ", foodPoisoning=" + foodPoisoning +
                '}';
    }
}
