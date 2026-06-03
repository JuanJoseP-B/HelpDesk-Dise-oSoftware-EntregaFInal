package com.helpdesk.application.command;

import com.helpdesk.domain.enums.NivelPrioridad;

/**
 * Comando para cambiar la prioridad de un ticket.
 */
public record CambiarPrioridadCommand(
        Long incidenciaId,
        Long adminId,
        NivelPrioridad nuevaPrioridad,
        String motivo
) {
}
