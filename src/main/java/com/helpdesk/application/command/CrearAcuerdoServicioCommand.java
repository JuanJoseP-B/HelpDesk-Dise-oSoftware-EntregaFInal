package com.helpdesk.application.command;

import com.helpdesk.domain.enums.NivelPrioridad;

/**
 * Comando de creación de acuerdo de servicio.
 */
public record CrearAcuerdoServicioCommand(
        String nombre,
        String descripcion,
        NivelPrioridad nivelPrioridad,
        int tiempoMaxRespuestaHoras,
        int tiempoMaxResolucionHoras
) {
}
