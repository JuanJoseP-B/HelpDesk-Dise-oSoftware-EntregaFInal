package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Solicitud para asignar un ticket a un técnico.
 */
public record AsignarTicketRequestDTO(
        @NotNull Long tecnicoId,
        String motivo
) {
}
