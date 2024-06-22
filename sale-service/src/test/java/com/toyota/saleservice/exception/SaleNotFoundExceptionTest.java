package com.toyota.saleservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaleNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Sale not found";
        SaleNotFoundException exception = new SaleNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
