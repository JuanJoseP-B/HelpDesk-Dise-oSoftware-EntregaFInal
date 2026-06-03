package com.helpdesk.application.command;

/**
 * Comando para reabrir un ticket cerrado.
 */
public record ReabrirTicketCommand(
        Long incidenciaId,
        Long clienteId,
        String motivo
) {
}
