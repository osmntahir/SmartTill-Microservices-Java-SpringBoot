package com.toyota.saleservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductQuantityShortageExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Product quantity shortage";
        ProductQuantityShortageException exception = new ProductQuantityShortageException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
