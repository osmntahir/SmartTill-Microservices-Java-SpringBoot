package com.toyota.saleservice.exception;

public class ProductAlreadyInCampaignException extends RuntimeException {
    public ProductAlreadyInCampaignException(String message) {
        super(message);
    }
}
