package com.helpdesk.application.dto.response;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;

import java.time.LocalDateTime;

/**
 * Proyección resumida de una incidencia para listados.
 */
public record IncidenciaSummaryDTO(
        Long id,
        String titulo,
        EstadoIncidencia estado,
        NivelPrioridad prioridad,
        LocalDateTime fechaCreacion
) {
}
