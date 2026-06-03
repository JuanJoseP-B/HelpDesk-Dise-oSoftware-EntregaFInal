package com.helpdesk.domain.exception;

/**
 * Excepción base del dominio.
 */
public class DomainException extends RuntimeException {

    public DomainException(String mensaje) {
        super(mensaje);
    }

    public DomainException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
