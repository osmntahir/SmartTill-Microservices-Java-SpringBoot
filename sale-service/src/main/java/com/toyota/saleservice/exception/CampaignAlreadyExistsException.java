package com.toyota.saleservice.exception;



/**
 * CampaignAlreadyExistsException thrown when campaign already exists
 */
public class CampaignAlreadyExistsException extends RuntimeException{
    public CampaignAlreadyExistsException(String message) {
        super(message);
    }
}
