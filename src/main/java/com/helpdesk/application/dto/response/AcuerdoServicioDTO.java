package com.helpdesk.application.dto.response;

import com.helpdesk.domain.enums.NivelPrioridad;

/**
 * Proyección de un acuerdo de servicio (SLA).
 */
public record AcuerdoServicioDTO(
        Long id,
        String nombre,
        NivelPrioridad prioridad,
        int tiempoMaxRespuestaHoras,
        int tiempoMaxResolucionHoras,
        boolean activo
) {
}
