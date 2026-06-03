package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.pagination.Pagina;
import com.helpdesk.domain.pagination.Paginacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.function.Function;

public final class PaginaMapper {

    private PaginaMapper() {
    }

    public static Pageable toPageable(Paginacion paginacion) {
        return PageRequest.of(paginacion.numeroPagina(), paginacion.tamanoPagina());
    }

    public static <T, R> Pagina<R> toPagina(Page<T> page, Function<T, R> mapper) {
        return new Pagina<>(
                page.getContent().stream().map(mapper).toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize()
        );
    }
}
