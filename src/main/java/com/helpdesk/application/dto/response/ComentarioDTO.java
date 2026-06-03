package com.helpdesk.application.dto.response;

import java.time.LocalDateTime;

/**
 * Proyección de un comentario de incidencia.
 */
public record ComentarioDTO(
        Long id,
        String contenido,
        Long autorId,
        LocalDateTime fechaHora,
        boolean visibleParaCliente
) {
}
