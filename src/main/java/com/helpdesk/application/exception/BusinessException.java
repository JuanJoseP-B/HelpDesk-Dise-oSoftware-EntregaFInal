package com.helpdesk.application.exception;

/**
 * Excepción de reglas de negocio o conflictos de estado.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String mensaje) {
        super(mensaje);
    }
}
