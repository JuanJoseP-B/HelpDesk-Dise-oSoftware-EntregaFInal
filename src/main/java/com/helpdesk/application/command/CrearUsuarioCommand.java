package com.helpdesk.application.command;

import com.helpdesk.domain.enums.RolUsuario;

/**
 * Comando de creación de usuario.
 */
public record CrearUsuarioCommand(
        String nombre,
        String email,
        String password,
        String telefono,
        RolUsuario rol
) {
}
