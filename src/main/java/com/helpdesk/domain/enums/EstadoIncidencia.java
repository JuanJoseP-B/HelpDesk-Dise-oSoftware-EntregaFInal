package com.helpdesk.domain.enums;

/**
 * Estados del ciclo de vida de una incidencia.
 * Flujo normal: ABIERTO → ASIGNADO → EN_PROGRESO → RESUELTO → CERRADO.
 */
public enum EstadoIncidencia {
    ABIERTO,
    ASIGNADO,
    EN_PROGRESO,
    RESUELTO,
    CERRADO
}
