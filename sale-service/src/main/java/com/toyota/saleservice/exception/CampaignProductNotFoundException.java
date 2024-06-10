package com.toyota.saleservice.exception;

public class CampaignProductNotFoundException extends RuntimeException{
    public CampaignProductNotFoundException(String message) {
        super(message);
    }
}
