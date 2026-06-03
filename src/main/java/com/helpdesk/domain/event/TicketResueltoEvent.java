package com.helpdesk.domain.event;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento de dominio emitido al resolver una incidencia.
 */
public final class TicketResueltoEvent {

    private final Long ticketId;
    private final Long tecnicoId;
    private final String solucion;
    private final LocalDateTime fechaResolucion;

    public TicketResueltoEvent(
            Long ticketId,
            Long tecnicoId,
            String solucion,
            LocalDateTime fechaResolucion
    ) {
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId");
        this.tecnicoId = Objects.requireNonNull(tecnicoId, "tecnicoId");
        this.solucion = Objects.requireNonNull(solucion, "solucion");
        this.fechaResolucion = Objects.requireNonNull(fechaResolucion, "fechaResolucion");
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public String getSolucion() {
        return solucion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }
}
