package com.helpdesk.application.dto.response;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;

import java.time.LocalDateTime;

/**
 * Respuesta estándar de una incidencia tras operaciones de escritura.
 */
public record IncidenciaResponseDTO(
        Long id,
        String titulo,
        String descripcion,
        EstadoIncidencia estado,
        NivelPrioridad prioridad,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaAsignacion,
        LocalDateTime fechaResolucion,
        LocalDateTime fechaCierre,
        Long clienteId,
        Long tecnicoAsignadoId,
        boolean slaViolado
) {
}
