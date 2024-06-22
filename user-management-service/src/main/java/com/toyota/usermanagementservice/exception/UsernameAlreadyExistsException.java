package com.toyota.usermanagementservice.exception;

import jakarta.validation.constraints.NotBlank;

/**
 * Exception thrown when attempting to create a user with a username that already exists.
 */
public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(@NotBlank(message = "Username must not be blank") String message) {
        super(message);
    }
}
