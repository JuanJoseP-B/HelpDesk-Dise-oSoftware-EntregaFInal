package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud de registro de un nuevo cliente.
 */
public record RegistroRequestDTO(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @Size(max = 20) String telefono
) {
}
