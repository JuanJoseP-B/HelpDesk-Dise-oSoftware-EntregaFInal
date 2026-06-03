package com.helpdesk.application.command;

/**
 * Comando para resolver un ticket.
 */
public record ResolverTicketCommand(
        Long incidenciaId,
        Long tecnicoId,
        String solucion
) {
}
