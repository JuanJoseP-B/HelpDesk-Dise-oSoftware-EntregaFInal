package com.helpdesk.domain.pagination;

/**
 * Criterio de paginación independiente del framework de persistencia.
 */
public record Paginacion(int numeroPagina, int tamanoPagina) {

    public Paginacion {
        if (numeroPagina < 0) {
            throw new IllegalArgumentException("numeroPagina no puede ser negativo");
        }
        if (tamanoPagina <= 0) {
            throw new IllegalArgumentException("tamanoPagina debe ser mayor que cero");
        }
    }

    /**
     * Calcula el desplazamiento (offset) para consultas paginadas.
     */
    public int getOffset() {
        return numeroPagina * tamanoPagina;
    }
}
