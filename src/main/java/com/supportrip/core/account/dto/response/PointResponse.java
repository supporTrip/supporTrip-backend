package com.supportrip.core.account.dto.response;

import com.supportrip.core.account.domain.PointTransaction;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class PointResponse {
    private final String transactionDate;
    private final String detail;
    private final String type;
    private final Long point;
    private final Long totalPoint;

    @Builder(access = AccessLevel.PRIVATE)

    public PointResponse(String transactionDate, String detail, String type, Long point, Long totalPoint) {
        this.transactionDate = transactionDate;
        this.detail = detail;
        this.type = type;
        this.point = point;
        this.totalPoint = totalPoint;
    }

    public static PointResponse of(String transactionDate, String detail, String type, Long point, Long totalPoint){
        return PointResponse.builder()
                .transactionDate(transactionDate)
                .detail(detail)
                .type(type)
                .point(point)
                .totalPoint(totalPoint)
                .build();
    }
}
