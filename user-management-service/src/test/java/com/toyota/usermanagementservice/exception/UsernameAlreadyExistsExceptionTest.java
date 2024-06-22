package com.toyota.usermanagementservice.exception;

import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsernameAlreadyExistsExceptionTest {

    @Test
    void testExceptionMessage() {
        String message = "Username must not be blank";
        UsernameAlreadyExistsException exception = new UsernameAlreadyExistsException(message);

        assertEquals(message, exception.getMessage());
    }
}
