package com.supportrip.core.system.core.insurance.internal.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "special_contract")
public class SpecialContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_insurance_id")
    private FlightInsurance flightInsurance;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "standard_price", nullable = false)
    private int standardPrice;

    @Column(name = "advanced_price", nullable = false)
    private int advancedPrice;

    public void setFlightInsurance(FlightInsurance flightInsurance) {
        this.flightInsurance = flightInsurance;
    }

    @Builder(access = AccessLevel.PRIVATE)
    private SpecialContract(Long id, FlightInsurance flightInsurance, String name, String description, int standardPrice, int advancedPrice) {
        this.id = id;
        this.flightInsurance = flightInsurance;
        this.name = name;
        this.description = description;
        this.standardPrice = standardPrice;
        this.advancedPrice = advancedPrice;
    }

    public static SpecialContract of(FlightInsurance flightInsurance, String name, int standardPrice, int advancedPrice) {
        return builder()
                .flightInsurance(flightInsurance)
                .name(name)
                .standardPrice(standardPrice)
                .advancedPrice(advancedPrice)
                .build();
    }

    public static SpecialContract create(FlightInsurance insurance, String name, String description, int standardPrice, int advancedPrice) {
        return SpecialContract.builder()
                .flightInsurance(insurance)
                .name(name)
                .description(description)
                .standardPrice(standardPrice)
                .advancedPrice(advancedPrice)
                .build();
    }

    public void update(String name, String description, int standardPrice, int advancedPrice, FlightInsurance flightInsurance) {
        this.name = name;
        this.description = description;
        this.standardPrice = standardPrice;
        this.advancedPrice = advancedPrice;
        this.flightInsurance = flightInsurance;
    }
}
