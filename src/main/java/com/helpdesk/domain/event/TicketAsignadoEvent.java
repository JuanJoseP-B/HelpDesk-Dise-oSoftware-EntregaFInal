package com.helpdesk.domain.event;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento de dominio emitido al asignar una incidencia a un técnico.
 */
public final class TicketAsignadoEvent {

    private final Long ticketId;
    private final Long tecnicoId;
    private final Long asignadoPorId;
    private final LocalDateTime fechaAsignacion;

    public TicketAsignadoEvent(
            Long ticketId,
            Long tecnicoId,
            Long asignadoPorId,
            LocalDateTime fechaAsignacion
    ) {
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId");
        this.tecnicoId = Objects.requireNonNull(tecnicoId, "tecnicoId");
        this.asignadoPorId = Objects.requireNonNull(asignadoPorId, "asignadoPorId");
        this.fechaAsignacion = Objects.requireNonNull(fechaAsignacion, "fechaAsignacion");
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public Long getAsignadoPorId() {
        return asignadoPorId;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }
}
