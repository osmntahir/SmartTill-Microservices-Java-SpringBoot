package com.toyota.usermanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    CASHIER,
    STORE_MANAGER,
    ADMIN;

    @JsonCreator
    public static Role forValue(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toUpperCase();
    }
}
