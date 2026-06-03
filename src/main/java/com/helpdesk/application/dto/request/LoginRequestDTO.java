package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Solicitud de inicio de sesión.
 */
public record LoginRequestDTO(
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
