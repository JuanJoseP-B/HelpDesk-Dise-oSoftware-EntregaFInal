package com.helpdesk.application.dto.response;

/**
 * Rendimiento de un técnico en un periodo.
 */
public record TecnicoPerformanceDTO(
        Long tecnicoId,
        String nombre,
        long asignadas,
        long resueltas,
        double promedioHorasResolucion
) {
}
