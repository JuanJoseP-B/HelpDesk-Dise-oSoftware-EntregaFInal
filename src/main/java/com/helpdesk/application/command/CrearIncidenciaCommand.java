package com.helpdesk.application.command;

import com.helpdesk.domain.enums.NivelPrioridad;

/**
 * Comando de creación de incidencia.
 */
public record CrearIncidenciaCommand(
        Long clienteId,
        String titulo,
        String descripcion,
        NivelPrioridad prioridad
) {
}
