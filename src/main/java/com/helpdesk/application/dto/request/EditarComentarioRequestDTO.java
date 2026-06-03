package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para editar un comentario existente.
 */
public record EditarComentarioRequestDTO(
        @NotBlank @Size(max = 3000) String contenido
) {
}
