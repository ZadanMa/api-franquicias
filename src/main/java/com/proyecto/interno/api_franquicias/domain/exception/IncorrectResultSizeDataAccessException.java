package com.proyecto.interno.api_franquicias.domain.exception;

public class IncorrectResultSizeDataAccessException extends RuntimeException {
    public IncorrectResultSizeDataAccessException(String message) {
        super(message);
    }
}