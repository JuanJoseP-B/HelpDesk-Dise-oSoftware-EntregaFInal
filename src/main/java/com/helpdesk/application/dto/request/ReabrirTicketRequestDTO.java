package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para reabrir un ticket cerrado.
 */
public record ReabrirTicketRequestDTO(
        @NotBlank @Size(max = 1000) String motivo
) {
}
