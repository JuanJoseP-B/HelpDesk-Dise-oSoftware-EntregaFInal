package com.helpdesk.application.dto.request;

import com.helpdesk.domain.enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Solicitud de creación de usuario por administrador.
 */
public record CrearUsuarioRequestDTO(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @Size(max = 20) String telefono,
        @NotNull RolUsuario rol
) {
}
