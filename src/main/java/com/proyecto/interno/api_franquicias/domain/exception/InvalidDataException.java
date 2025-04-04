package com.proyecto.interno.api_franquicias.domain.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}