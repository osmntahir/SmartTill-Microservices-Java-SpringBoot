package com.toyota.saleservice.exception;


/**
 *  CampaignNotFoundException thrown when campaign not found
 */
public class CampaignNotFoundException extends RuntimeException{
    public CampaignNotFoundException(String message) {
        super(message);

    }
}

