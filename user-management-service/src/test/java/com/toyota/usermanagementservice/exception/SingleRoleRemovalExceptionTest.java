package com.toyota.usermanagementservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleRoleRemovalExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Cannot remove the last role assigned to the user";
        SingleRoleRemovalException exception = new SingleRoleRemovalException(message);

        assertEquals(message, exception.getMessage());
    }
}
