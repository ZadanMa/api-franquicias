package com.proyecto.interno.api_franquicias.domain.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidFranquiciaExceptionTest {

    @Test
    void testExceptionMessageAndToString() {
        String mensaje = "Mensaje de error";
        InvalidFranquiciaException exception = new InvalidFranquiciaException(mensaje);
        assertEquals(mensaje, exception.getMessage());
        assertNotNull(exception.toString());
    }
}
