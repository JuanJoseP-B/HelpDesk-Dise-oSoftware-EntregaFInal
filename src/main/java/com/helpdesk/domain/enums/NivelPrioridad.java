package com.helpdesk.domain.enums;

/**
 * Niveles de prioridad con tiempos SLA en horas (respuesta / resolución).
 */
public enum NivelPrioridad {
    BAJA(24, 120),
    MEDIA(8, 48),
    ALTA(2, 8),
    CRITICA(1, 4);

    private final int horasMaxRespuesta;
    private final int horasMaxResolucion;

    NivelPrioridad(int horasMaxRespuesta, int horasMaxResolucion) {
        this.horasMaxRespuesta = horasMaxRespuesta;
        this.horasMaxResolucion = horasMaxResolucion;
    }

    public int getHorasMaxRespuesta() {
        return horasMaxRespuesta;
    }

    public int getHorasMaxResolucion() {
        return horasMaxResolucion;
    }
}
