package com.toyota.saleservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CampaignProductNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Campaign product not found";
        CampaignProductNotFoundException exception = new CampaignProductNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
