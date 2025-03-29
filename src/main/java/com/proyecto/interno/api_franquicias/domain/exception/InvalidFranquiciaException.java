package com.proyecto.interno.api_franquicias.domain.exception;

public class InvalidFranquiciaException extends RuntimeException {
    public InvalidFranquiciaException(String message) {
        super(message);
    }
}
