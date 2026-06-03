package com.helpdesk.domain.pagination;

import java.util.List;

/**
 * Resultado paginado de una consulta de dominio.
 *
 * @param <T> tipo de elemento contenido
 */
public record Pagina<T>(
        List<T> contenido,
        long totalElementos,
        int numeroPagina,
        int tamanoPagina
) {

    public Pagina {
        contenido = List.copyOf(contenido);
    }

    /**
     * Número total de páginas según el tamaño configurado.
     */
    public int getTotalPaginas() {
        if (tamanoPagina == 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElementos / tamanoPagina);
    }

    public boolean tieneSiguiente() {
        return numeroPagina + 1 < getTotalPaginas();
    }

    public boolean tieneAnterior() {
        return numeroPagina > 0;
    }
}
