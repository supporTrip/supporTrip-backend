package com.supportrip.core.system.core.user.internal.domain;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum UserCardApprovalStatus {
    APPROVED("01", "승인"),
    CANCELLED("02","승인취소"),
    CORRECTED("03", "정정"),
    UNAPPROVED_PURCHASE("04", "무승인매입");

    private String code;
    private String value;


    private static final Map<String, UserCardApprovalStatus> codeMap = new HashMap<>();

    static {
        for (UserCardApprovalStatus status : UserCardApprovalStatus.values()) {
            codeMap.put(status.getCode(), status);
        }
    }

    UserCardApprovalStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static UserCardApprovalStatus findByCode(String code) {
        return codeMap.get(code);
    }
}