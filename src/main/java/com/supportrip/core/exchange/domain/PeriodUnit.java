package com.supportrip.core.exchange.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;

import static java.time.temporal.ChronoUnit.MONTHS;

public enum PeriodUnit {
    ONE_MONTH(1, MONTHS), THREE_MONTH(3, MONTHS);

    private final long pastTimeAmount;
    private final TemporalUnit temporalUnit;

    PeriodUnit(long pastTimeAmount, TemporalUnit temporalUnit) {
        this.pastTimeAmount = pastTimeAmount;
        this.temporalUnit = temporalUnit;
    }

    public LocalDateTime getPastDateTime(LocalDateTime dateTime, boolean pastDateInclusive) {
        LocalDate pastDate = dateTime.minus(pastTimeAmount, temporalUnit).toLocalDate();
        LocalTime localTime = pastDateInclusive ? LocalTime.MIN : LocalTime.MAX;

        return LocalDateTime.of(pastDate, localTime);
    }
}
