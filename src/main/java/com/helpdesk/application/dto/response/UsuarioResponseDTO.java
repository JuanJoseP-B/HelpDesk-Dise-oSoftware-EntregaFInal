package com.helpdesk.application.dto.response;

import com.helpdesk.domain.enums.RolUsuario;

import java.time.LocalDateTime;

/**
 * Respuesta con datos públicos de un usuario.
 */
public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String email,
        String telefono,
        RolUsuario rol,
        boolean activo,
        LocalDateTime fechaRegistro
) {
}
