package com.helpdesk.application.dto.request;

import com.helpdesk.domain.enums.RolUsuario;
import jakarta.validation.constraints.NotNull;

/**
 * Solicitud de cambio de rol de usuario.
 */
public record CambiarRolRequestDTO(
        @NotNull RolUsuario nuevoRol
) {
}
