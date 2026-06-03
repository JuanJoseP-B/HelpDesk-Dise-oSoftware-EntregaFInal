package com.helpdesk.domain.event;

import com.helpdesk.domain.enums.NivelPrioridad;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Evento de dominio emitido cuando se viola un acuerdo de nivel de servicio.
 */
public final class SlaVioladoEvent {

    private final Long ticketId;
    private final NivelPrioridad prioridad;
    private final LocalDateTime fechaVencimiento;
    private final String tipoSla;

    public SlaVioladoEvent(
            Long ticketId,
            NivelPrioridad prioridad,
            LocalDateTime fechaVencimiento,
            String tipoSla
    ) {
        this.ticketId = Objects.requireNonNull(ticketId, "ticketId");
        this.prioridad = Objects.requireNonNull(prioridad, "prioridad");
        this.fechaVencimiento = Objects.requireNonNull(fechaVencimiento, "fechaVencimiento");
        this.tipoSla = Objects.requireNonNull(tipoSla, "tipoSla");
    }

    public Long getTicketId() {
        return ticketId;
    }

    public NivelPrioridad getPrioridad() {
        return prioridad;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getTipoSla() {
        return tipoSla;
    }
}
