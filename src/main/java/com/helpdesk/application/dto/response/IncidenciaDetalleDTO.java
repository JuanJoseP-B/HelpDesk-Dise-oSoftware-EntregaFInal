package com.helpdesk.application.dto.response;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Vista detallada de una incidencia con historial y comentarios.
 */
public record IncidenciaDetalleDTO(
        Long id,
        String titulo,
        EstadoIncidencia estado,
        NivelPrioridad prioridad,
        LocalDateTime fechaCreacion,
        String descripcion,
        String solucion,
        Long clienteId,
        Long tecnicoAsignadoId,
        boolean slaViolado,
        List<HistorialEstadoDTO> historial,
        List<ComentarioDTO> comentarios
) {
}
