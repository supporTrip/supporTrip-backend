package com.supportrip.core.system.core.auth.internal.domain;

public enum Role {
    USER, ADMIN;

    public String toRoleKey() {
        return "ROLE_" + this.name();
    }
}
