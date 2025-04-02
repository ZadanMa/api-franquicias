package com.proyecto.interno.api_franquicias.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testConstructorAndGetterSetter() {
        // Se crea la instancia con un mensaje inicial
        ErrorResponse errorResponse = new ErrorResponse("Mensaje de error");
        assertEquals("Mensaje de error", errorResponse.getMensaje());

        // Se modifica el mensaje y se verifica
        errorResponse.setMensaje("Nuevo mensaje");
        assertEquals("Nuevo mensaje", errorResponse.getMensaje());
    }
}
