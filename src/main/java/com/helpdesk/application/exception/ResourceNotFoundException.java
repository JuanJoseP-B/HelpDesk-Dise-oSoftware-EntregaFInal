package com.helpdesk.application.exception;

/**
 * Excepción cuando no se encuentra un recurso solicitado.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " no encontrado con id: " + id);
    }
}
