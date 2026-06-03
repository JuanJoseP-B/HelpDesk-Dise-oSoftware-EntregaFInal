package com.helpdesk.domain.exception;

import com.helpdesk.domain.enums.EstadoIncidencia;

/**
 * Se lanza cuando una transición de estado no es válida para el rol o el estado actual.
 */
public class TransicionEstadoInvalidaException extends DomainException {

    public TransicionEstadoInvalidaException(EstadoIncidencia actual, EstadoIncidencia solicitado) {
        super(String.format(
                "Transición inválida de %s a %s",
                actual,
                solicitado
        ));
    }

    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }
}
