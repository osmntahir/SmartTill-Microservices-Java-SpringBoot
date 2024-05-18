package com.toyota.saleservice.exception;



/**
 * VehicleAlreadyExistsException thrown when vehicle already exists
 */
public class CampaignAlreadyExistsException extends RuntimeException{
    public CampaignAlreadyExistsException(String message) {
        super(message);
    }
}
