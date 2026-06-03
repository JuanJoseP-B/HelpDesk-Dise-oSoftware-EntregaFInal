package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para agregar un comentario a una incidencia.
 */
public record CrearComentarioRequestDTO(
        @NotBlank @Size(max = 3000) String contenido,
        boolean visibleCliente
) {
}
