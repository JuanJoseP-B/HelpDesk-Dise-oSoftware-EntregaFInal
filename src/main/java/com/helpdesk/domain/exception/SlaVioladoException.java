package com.helpdesk.domain.exception;

/**
 * Se lanza cuando se detecta una violación de SLA en una incidencia.
 */
public class SlaVioladoException extends DomainException {

    public SlaVioladoException(Long ticketId, String tipoSla) {
        super(String.format("SLA violado en ticket %d (%s)", ticketId, tipoSla));
    }
}
