package com.supportrip.core.insurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SearchFlightInsuranceRequestDTO {
    @NotNull(message = "출발시간을 입력해주세요")
    private LocalDateTime departAt;

    @NotNull(message = "도착시간을 입력해주세요")
    private LocalDateTime arrivalAt;

    @NotNull(message = "생년월일 6자리를 입력해주세요.")
    private LocalDate birthDay;

    @NotBlank(message = "성별을 입력해주세요.")
    private String gender;

    private String planName;
    private Boolean overseasMedicalExpenses;
    private Boolean phoneLoss;
    private Boolean flightDelay;
    private Boolean passportLoss;
    private Boolean foodPoisoning;

    @Builder
    public SearchFlightInsuranceRequestDTO(LocalDateTime departAt, LocalDateTime arrivalAt, LocalDate birthDay, String gender, String planName, Boolean overseasMedicalExpenses, Boolean phoneLoss, Boolean flightDelay, Boolean passportLoss, Boolean foodPoisoning) {
        this.departAt = departAt;
        this.arrivalAt = arrivalAt;
        this.birthDay = birthDay;
        this.gender = gender;
        this.planName = planName == null ? "standard" : planName;
        this.overseasMedicalExpenses = overseasMedicalExpenses == null ? Boolean.FALSE : overseasMedicalExpenses;
        this.phoneLoss = phoneLoss == null ? Boolean.FALSE : phoneLoss;
        this.flightDelay = flightDelay == null ? Boolean.FALSE : flightDelay;
        this.passportLoss = passportLoss == null ? Boolean.FALSE : passportLoss;
        this.foodPoisoning = foodPoisoning == null ? Boolean.FALSE : foodPoisoning;
    }

    @Builder
    public static SearchFlightInsuranceRequestDTO of(LocalDateTime departAt, LocalDateTime arrivalAt, LocalDate birthDay, String gender, String planName, Boolean overseasMedicalExpenses, Boolean phoneLoss, Boolean flightDelay, Boolean passportLoss, Boolean foodPoisoning) {
        return builder()
                .departAt(departAt)
                .arrivalAt(arrivalAt)
                .birthDay(birthDay)
                .gender(gender)
                .planName(planName)
                .overseasMedicalExpenses(overseasMedicalExpenses)
                .phoneLoss(phoneLoss)
                .flightDelay(flightDelay)
                .passportLoss(passportLoss)
                .foodPoisoning(foodPoisoning)
                .build();
    }
}
