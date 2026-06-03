package com.helpdesk.application.dto.response;

/**
 * Indicadores de cumplimiento de SLA en un periodo.
 */
public record SlaCumplimientoDTO(
        String periodo,
        long total,
        long cumplidos,
        long violados,
        double porcentajeCumplimiento
) {
}
