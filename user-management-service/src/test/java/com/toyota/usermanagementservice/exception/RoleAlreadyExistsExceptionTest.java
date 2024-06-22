package com.toyota.usermanagementservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleAlreadyExistsExceptionTest {

    @Test
    void testExceptionMessage() {
        String roleName = "ADMIN";
        RoleAlreadyExistsException exception = new RoleAlreadyExistsException(roleName);

        assertEquals(roleName, exception.getMessage());
    }
}
