package com.toyota.saleservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaleAlreadyExistsExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Sale already exists";
        SaleAlreadyExistsException exception = new SaleAlreadyExistsException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
