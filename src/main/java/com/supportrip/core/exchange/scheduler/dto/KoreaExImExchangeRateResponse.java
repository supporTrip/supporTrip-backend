package com.supportrip.core.exchange.scheduler.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KoreaExImExchangeRateResponse {
    private Integer result;
    private String curUnit;
    private String curNm;
    private String ttb;
    private String tts;
    private String dealBasR;
    private String bkpr;
    private String yyEfeeR;
    private String tenDdEfeeR;
    private String kftcDealBasR;
    private String kftcBkpr;

    public KoreaExImExchangeRateResponse(Integer result, String curUnit, String curNm, String ttb, String tts, String dealBasR, String bkpr, String yyEfeeR, String tenDdEfeeR, String kftcDealBasR, String kftcBkpr) {
        this.result = result;
        this.curUnit = curUnit;
        this.curNm = curNm;
        this.ttb = ttb;
        this.tts = tts;
        this.dealBasR = dealBasR;
        this.bkpr = bkpr;
        this.yyEfeeR = yyEfeeR;
        this.tenDdEfeeR = tenDdEfeeR;
        this.kftcDealBasR = kftcDealBasR;
        this.kftcBkpr = kftcBkpr;
    }
}
