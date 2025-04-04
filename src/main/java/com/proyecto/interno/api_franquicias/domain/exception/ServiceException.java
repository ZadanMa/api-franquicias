package com.proyecto.interno.api_franquicias.domain.exception;

public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}