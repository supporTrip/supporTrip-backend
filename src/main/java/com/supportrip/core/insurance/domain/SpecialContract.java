package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "special_contract_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_insurance_id")
    private FlightInsurance flightInsurance;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private int basicPrice;

    @NotNull
    private int standardPrice;

    @NotNull
    private int advancedPrice;

    public void setFlightInsurance(FlightInsurance flightInsurance) {
        this.flightInsurance = flightInsurance;
    }

    @Builder
    private SpecialContract(Long id, FlightInsurance flightInsurance, String name, String description, int basicPrice, int standardPrice, int advancedPrice) {
        this.id = id;
        this.flightInsurance = flightInsurance;
        this.name = name;
        this.description = description;
        this.basicPrice = basicPrice;
        this.standardPrice = standardPrice;
        this.advancedPrice = advancedPrice;
    }

    //==정적 팩토리 메서드==//
    public static SpecialContract of(FlightInsurance flightInsurance, String name, int basicPrice, int standardPrice, int advancedPrice) {
        return builder()
                .flightInsurance(flightInsurance)
                .name(name)
                .basicPrice(basicPrice)
                .standardPrice(standardPrice)
                .advancedPrice(advancedPrice)
                .build();
    }
}
