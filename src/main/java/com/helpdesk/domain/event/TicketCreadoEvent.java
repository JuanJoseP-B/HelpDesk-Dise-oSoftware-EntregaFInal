package com.helpdesk.domain.event;

import com.helpdesk.domain.enums.NivelPrioridad;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento de dominio emitido al crear una nueva incidencia.
 */
public final class TicketCreadoEvent {

    private final Long ticketId;
    private final Long clienteId;
    private final LocalDateTime fechaCreacion;
    private final NivelPrioridad prioridad;

    public TicketCreadoEvent(Long ticketId, Long clienteId, LocalDateTime fechaCreacion, NivelPrioridad prioridad) {
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId");
        this.clienteId = Objects.requireNonNull(clienteId, "clienteId");
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "fechaCreacion");
        this.prioridad = Objects.requireNonNull(prioridad, "prioridad");
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public NivelPrioridad getPrioridad() {
        return prioridad;
    }
}
