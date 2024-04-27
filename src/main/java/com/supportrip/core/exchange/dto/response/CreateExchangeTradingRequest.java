package com.supportrip.core.exchange.dto.response;

import lombok.Getter;

import java.time.LocalDate;

/*
  airplainCertifactionId: null,
  ticketPnrNumber: null,
  departAt: null,

  completeDate: null,
  targetCurrency: null,
  targetCurrencyName: null,

  tradingAmount: null,
  currentExchangeRate: null,

  strategy: null,
  targetExchangeRate: null,
  point: null,
  availablePoint: null,

  startingExchangeRate: null,
  displayName: '',
 */
@Getter
public class CreateExchangeTradingRequest {
     private String displayName;
     private Long airplainCertifactionId;
     private Long targetCurrencyId;
     private Long tradingAmount;
     private Double currentExchangeRate;
     private String strategy;
     private Double targetExchangeRate;
     private Long startingExchangeRateId;
     private LocalDate completeDate;
//    private Long point;

}
