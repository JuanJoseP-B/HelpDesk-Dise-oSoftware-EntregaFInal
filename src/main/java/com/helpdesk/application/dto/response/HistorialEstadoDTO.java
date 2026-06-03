package com.helpdesk.application.dto.response;

import com.helpdesk.domain.enums.EstadoIncidencia;

import java.time.LocalDateTime;

/**
 * Proyección de un registro de historial de estado.
 */
public record HistorialEstadoDTO(
        Long id,
        EstadoIncidencia estadoAnterior,
        EstadoIncidencia estadoNuevo,
        LocalDateTime fechaCambio,
        Long usuarioId,
        String motivo
) {
}
