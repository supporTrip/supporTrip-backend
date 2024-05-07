package com.supportrip.core.system.core.airplane.internal.domain;

import com.supportrip.core.system.core.exchange.internal.domain.Country;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "airplain_certification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "createdAt", column = @Column(name = "certified_at"))
public class AirplaneCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(name = "pnr_number", nullable = false)
    private String pnrNumber;

    @Column(name = "depart_at", nullable = false)
    private LocalDateTime departAt;

    @Column(name = "certification", nullable = false)
    private Boolean certification;

    @Builder(access = AccessLevel.PRIVATE)
    private AirplaneCertification(Country country, String pnrNumber, LocalDateTime departAt, Boolean certification) {
        this.country = country;
        this.pnrNumber = pnrNumber;
        this.departAt = departAt;
        this.certification = certification;
    }

    public static AirplaneCertification of(Country country, String pnrNumber, LocalDateTime departAt, Boolean certification) {
        return AirplaneCertification.builder()
                .country(country)
                .pnrNumber(pnrNumber)
                .departAt(departAt)
                .certification(certification)
                .build();
    }
}

