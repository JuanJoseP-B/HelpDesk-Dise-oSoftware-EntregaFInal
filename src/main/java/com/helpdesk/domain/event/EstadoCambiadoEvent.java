package com.helpdesk.domain.event;

import com.helpdesk.domain.enums.EstadoIncidencia;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento de dominio emitido al cambiar el estado de una incidencia.
 */
public final class EstadoCambiadoEvent {

    private final Long ticketId;
    private final EstadoIncidencia estadoAnterior;
    private final EstadoIncidencia estadoNuevo;
    private final Long usuarioId;
    private final LocalDateTime fechaCambio;
    private final String motivo;

    public EstadoCambiadoEvent(
            Long ticketId,
            EstadoIncidencia estadoAnterior,
            EstadoIncidencia estadoNuevo,
            Long usuarioId,
            LocalDateTime fechaCambio,
            String motivo
    ) {
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId");
        this.estadoAnterior = Objects.requireNonNull(estadoAnterior, "estadoAnterior");
        this.estadoNuevo = Objects.requireNonNull(estadoNuevo, "estadoNuevo");
        this.usuarioId = Objects.requireNonNull(usuarioId, "usuarioId");
        this.fechaCambio = Objects.requireNonNull(fechaCambio, "fechaCambio");
        this.motivo = motivo;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public EstadoIncidencia getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoIncidencia getEstadoNuevo() {
        return estadoNuevo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public String getMotivo() {
        return motivo;
    }
}
