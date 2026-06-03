package com.helpdesk.application.dto.response;

import com.helpdesk.domain.enums.TipoNotificacion;

/**
 * Proyección de una notificación.
 */
public record NotificacionDTO(
        Long id,
        String asunto,
        String mensaje,
        TipoNotificacion tipo,
        String estadoEnvio,
        boolean leida
) {
}
