package com.helpdesk.application.command;

/**
 * Comando para iniciar trabajo en un ticket asignado.
 */
public record IniciarTrabajoCommand(
        Long incidenciaId,
        Long tecnicoId
) {
}
