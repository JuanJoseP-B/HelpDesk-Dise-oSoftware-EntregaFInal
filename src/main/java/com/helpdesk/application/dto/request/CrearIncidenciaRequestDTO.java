package com.helpdesk.application.dto.request;

import com.helpdesk.domain.enums.NivelPrioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Solicitud HTTP para crear una incidencia.
 */
public record CrearIncidenciaRequestDTO(
        @NotBlank @Size(max = 200) String titulo,
        @NotBlank @Size(max = 5000) String descripcion,
        @NotNull NivelPrioridad prioridad
) {
}
