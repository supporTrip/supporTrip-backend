package com.supportrip.core.insurance.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpecialContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_insurance_id")
    private TravelInsurance travelInsurance;

    private String name;

    private String description;

    private int basicPrice;

    private int standardPrice;

    private int advancedPrice;

}
