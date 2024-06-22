package com.toyota.saleservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CampaignNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Campaign not found";
        CampaignNotFoundException exception = new CampaignNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
