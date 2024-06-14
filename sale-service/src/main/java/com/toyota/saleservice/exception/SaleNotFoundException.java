package com.toyota.saleservice.exception;

public class SaleNotFoundException extends RuntimeException{
    public SaleNotFoundException(String s) {
        super(s);
    }
}
