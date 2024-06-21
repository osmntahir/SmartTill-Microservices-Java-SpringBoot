package com.toyota.usermanagementservice.exception;

import jakarta.validation.constraints.NotBlank;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(@NotBlank(message = "Username must not be blank") String s) {
        super(s);
    }
}
