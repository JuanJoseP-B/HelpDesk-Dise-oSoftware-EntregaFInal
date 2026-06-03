package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud para resolver un ticket.
 */
public record ResolverTicketRequestDTO(
        @NotBlank @Size(max = 5000) String solucion
) {
}
