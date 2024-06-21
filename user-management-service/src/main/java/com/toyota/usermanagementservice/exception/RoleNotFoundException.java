package com.toyota.usermanagementservice.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String s) {
        super(s);
    }
}
