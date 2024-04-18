package com.supportrip.core.auth.domain;

public enum Role {
    USER, ADMIN;

    public String toRoleKey() {
        return "ROLE_" + this.name();
    }
}
