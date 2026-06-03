package com.helpdesk.application.dto.response;

/**
 * Métricas agregadas del panel de administración.
 */
public record DashboardMetricsDTO(
        long totalIncidencias,
        long abiertas,
        long resueltas,
        long cerradas,
        long slaViolados,
        double promedioResolucionHoras
) {
}
