package com.helpdesk.domain.port;

import com.helpdesk.domain.entity.Notificacion;

/**
 * Puerto de salida para enviar notificaciones por canales externos.
 */
public interface NotificationPort {

    /**
     * Envia una notificacion previamente registrada.
     */
    void enviar(Notificacion notificacion);
}
