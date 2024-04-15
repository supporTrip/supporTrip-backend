package com.supportrip.core.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimpleIdResponse {
    private final Long id;

    @Builder(access = AccessLevel.PRIVATE)
    private SimpleIdResponse(Long id) {
        this.id = id;
    }

    public static SimpleIdResponse from(Long id) {
        return new SimpleIdResponse(id);
    }
}
