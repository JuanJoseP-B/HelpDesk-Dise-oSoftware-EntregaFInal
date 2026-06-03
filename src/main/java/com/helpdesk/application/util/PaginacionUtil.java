package com.helpdesk.application.util;

import com.helpdesk.domain.pagination.Paginacion;
import org.springframework.data.domain.Pageable;

/**
 * Utilidades de conversión entre paginación Spring y dominio.
 */
public final class PaginacionUtil {

    private PaginacionUtil() {
    }

    public static Paginacion fromPageable(Pageable pageable) {
        return new Paginacion(pageable.getPageNumber(), pageable.getPageSize());
    }
}
