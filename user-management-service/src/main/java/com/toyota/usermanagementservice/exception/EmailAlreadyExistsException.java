package com.toyota.usermanagementservice.exception;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Exception thrown when attempting to create a user with an email that already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(@Email(message = "Email must be valid") @NotBlank(message = "Email must not be blank") String message) {
        super(message);
    }
}
