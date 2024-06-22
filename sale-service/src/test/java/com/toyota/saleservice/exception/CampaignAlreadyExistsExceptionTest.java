package com.toyota.saleservice.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CampaignAlreadyExistsExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Campaign already exists";
        CampaignAlreadyExistsException exception = new CampaignAlreadyExistsException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }
}
