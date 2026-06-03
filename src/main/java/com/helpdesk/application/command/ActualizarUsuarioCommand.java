package com.helpdesk.application.command;

/**
 * Comando de actualización de usuario.
 */
public record ActualizarUsuarioCommand(
        Long usuarioId,
        String nombre,
        String telefono,
        Boolean activo
) {
}
