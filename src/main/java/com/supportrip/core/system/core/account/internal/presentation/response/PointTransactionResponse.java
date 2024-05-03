package com.supportrip.core.system.core.account.internal.presentation.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PointTransactionResponse {
    private final String transactionDate;
    private final String detail;
    private final String type;
    private final Long point;
    private final Long totalPoint;

    @Builder(access = AccessLevel.PRIVATE)

    public PointTransactionResponse(String transactionDate, String detail, String type, Long point, Long totalPoint) {
        this.transactionDate = transactionDate;
        this.detail = detail;
        this.type = type;
        this.point = point;
        this.totalPoint = totalPoint;
    }

    public static PointTransactionResponse of(String transactionDate, String detail, String type, Long point, Long totalPoint){
        return PointTransactionResponse.builder()
                .transactionDate(transactionDate)
                .detail(detail)
                .type(type)
                .point(point)
                .totalPoint(totalPoint)
                .build();
    }
}
