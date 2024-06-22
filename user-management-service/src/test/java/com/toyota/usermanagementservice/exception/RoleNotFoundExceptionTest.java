package com.toyota.usermanagementservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        String roleName = "ADMIN";
        RoleNotFoundException exception = new RoleNotFoundException(roleName);

        assertEquals(roleName, exception.getMessage());
    }
}
