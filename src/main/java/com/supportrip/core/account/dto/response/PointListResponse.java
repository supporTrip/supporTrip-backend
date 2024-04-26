package com.supportrip.core.account.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PointListResponse {
    private final Long userTotalPoint;
    private final List<PointResponse> details;

    @Builder(access = AccessLevel.PRIVATE)

    public PointListResponse(Long userTotalPoint, List<PointResponse> details) {
        this.userTotalPoint = userTotalPoint;
        this.details = details;
    }

    public static PointListResponse of(Long userTotalPoint, List<PointResponse> details){
        return PointListResponse.builder()
                .userTotalPoint(userTotalPoint)
                .details(details)
                .build();
    }
}
