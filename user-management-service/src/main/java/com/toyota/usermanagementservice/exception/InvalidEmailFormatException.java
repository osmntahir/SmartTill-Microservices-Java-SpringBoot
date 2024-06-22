package com.toyota.usermanagementservice.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Exception thrown when an invalid email format is detected.
 */
public class InvalidEmailFormatException extends RuntimeException {
    public InvalidEmailFormatException(@Email(message = "Email must be valid") @NotBlank(message = "Email must not be blank") String message) {
        super(message);
    }
}
