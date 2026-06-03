package com.helpdesk.application.command;

/**
 * Comando para cerrar un ticket resuelto.
 */
public record CerrarTicketCommand(
        Long incidenciaId,
        Long usuarioId
) {
}
