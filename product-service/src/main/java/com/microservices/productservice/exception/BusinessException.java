package com.microservices.productservice.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message) {
        super(message);
    }
}
