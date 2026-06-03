package com.helpdesk.application.dto.request;

import com.helpdesk.domain.enums.NivelPrioridad;
import jakarta.validation.constraints.NotNull;

/**
 * Solicitud para cambiar la prioridad de un ticket.
 */
public record CambiarPrioridadRequestDTO(
        @NotNull NivelPrioridad nuevaPrioridad,
        String motivo
) {
}
