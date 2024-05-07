package com.supportrip.core.system.core.exchange.internal.presentation.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreateExchangeTradingRequest {
     private String displayName;
     private Long countryId;
     private LocalDateTime departAt;
     private String pnrNumber;
     private Long targetCurrencyId;
     private Long tradingAmount;
     private String strategy;
     private Double targetExchangeRate;
     private Long startingExchangeRateId;
     private LocalDate completeDate;
     private Long point;

}
