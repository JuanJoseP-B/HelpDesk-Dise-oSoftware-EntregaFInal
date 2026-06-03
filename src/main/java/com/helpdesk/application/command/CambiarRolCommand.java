package com.helpdesk.application.command;

import com.helpdesk.domain.enums.RolUsuario;

/**
 * Comando de cambio de rol de usuario.
 */
public record CambiarRolCommand(
        Long usuarioId,
        Long adminId,
        RolUsuario nuevoRol
) {
}
