package com.toyota.saleservice.exception;

public class SaleAlreadyExistsException extends RuntimeException {
    public SaleAlreadyExistsException(String s) {
            super(s);
    }
}
