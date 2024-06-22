package com.toyota.saleservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Product not found";
        ProductNotFoundException exception = new ProductNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
