package com.toyota.usermanagementservice.exception;

/**
 * Exception thrown when a requested role is not found.
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
