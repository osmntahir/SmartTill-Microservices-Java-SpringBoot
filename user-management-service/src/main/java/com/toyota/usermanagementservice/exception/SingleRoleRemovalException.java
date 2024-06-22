package com.toyota.usermanagementservice.exception;

/**
 * Exception thrown when attempting to remove the last role from a user.
 */
public class SingleRoleRemovalException extends RuntimeException {
    public SingleRoleRemovalException(String message) {
        super(message);
    }
}
