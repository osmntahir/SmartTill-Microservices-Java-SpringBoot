package com.toyota.usermanagementservice.exception;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String s) {
        super(s);
    }
}
