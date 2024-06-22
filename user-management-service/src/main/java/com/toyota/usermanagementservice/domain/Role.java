package com.toyota.usermanagementservice.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enumeration representing roles in the user management system.
 */
public enum Role {
    CASHIER,
    STORE_MANAGER,
    ADMIN;

    /**
     * Creates a Role enum from a string value.
     *
     * @param value The string value representing the role
     * @return The Role enum corresponding to the given value
     */
    @JsonCreator
    public static Role forValue(String value) {
        return Role.valueOf(value.toUpperCase());
    }

    /**
     * Converts the Role enum to its corresponding string value.
     *
     * @return The string representation of the Role enum
     */
    @JsonValue
    public String toValue() {
        return name().toUpperCase();
    }
}
