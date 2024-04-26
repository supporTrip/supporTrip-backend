package com.supportrip.core.account.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PointTransactionListResponse {
    private final Long userTotalPoint;
    private final List<PointTransactionResponse> details;

    @Builder(access = AccessLevel.PRIVATE)

    public PointTransactionListResponse(Long userTotalPoint, List<PointTransactionResponse> details) {
        this.userTotalPoint = userTotalPoint;
        this.details = details;
    }

    public static PointTransactionListResponse of(Long userTotalPoint, List<PointTransactionResponse> details){
        return PointTransactionListResponse.builder()
                .userTotalPoint(userTotalPoint)
                .details(details)
                .build();
    }
}
