package com.toyota.usermanagementservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvalidEmailFormatExceptionTest {

    @Test
    void testExceptionMessage() {
        String invalidEmail = "invalidemail";
        InvalidEmailFormatException exception = new InvalidEmailFormatException(invalidEmail);

        assertEquals(invalidEmail, exception.getMessage());
    }
}
