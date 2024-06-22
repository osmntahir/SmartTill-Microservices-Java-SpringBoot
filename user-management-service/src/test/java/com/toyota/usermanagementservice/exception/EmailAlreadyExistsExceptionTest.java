package com.toyota.usermanagementservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailAlreadyExistsExceptionTest {

    @Test
    void testExceptionMessage() {
        String email = "test@example.com";
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(email);

        assertEquals(email, exception.getMessage());
    }
}
