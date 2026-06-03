package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Solicitud de actualización de datos de usuario.
 */
public record ActualizarUsuarioRequestDTO(
        @Size(max = 100) String nombre,
        @Size(max = 20) String telefono,
        Boolean activo
) {
}
