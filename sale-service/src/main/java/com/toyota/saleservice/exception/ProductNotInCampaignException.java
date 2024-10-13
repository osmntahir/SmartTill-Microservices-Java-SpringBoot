package com.toyota.saleservice.exception;

public class ProductNotInCampaignException extends RuntimeException {
    public ProductNotInCampaignException(String message) {
        super(message);
    }
}
