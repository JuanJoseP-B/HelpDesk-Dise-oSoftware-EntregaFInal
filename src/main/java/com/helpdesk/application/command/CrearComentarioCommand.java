package com.helpdesk.application.command;

/**
 * Comando para agregar un comentario a una incidencia.
 */
public record CrearComentarioCommand(
        Long incidenciaId,
        Long autorId,
        String contenido,
        boolean visibleCliente
) {
}
