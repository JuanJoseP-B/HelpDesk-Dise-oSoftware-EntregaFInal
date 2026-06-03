package com.helpdesk.application.command;

/**
 * Comando de asignación de ticket a técnico.
 */
public record AsignarTicketCommand(
        Long incidenciaId,
        Long tecnicoId,
        Long asignadoPorId,
        String motivo
) {
}
