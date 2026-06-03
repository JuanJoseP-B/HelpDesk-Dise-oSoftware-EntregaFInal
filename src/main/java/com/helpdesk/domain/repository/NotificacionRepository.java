package com.helpdesk.domain.repository;

import com.helpdesk.domain.entity.Notificacion;

import java.util.List;

/**
 * Puerto de persistencia para {@link Notificacion}.
 */
public interface NotificacionRepository {

    /**
     * Persiste o actualiza una notificación.
     */
    Notificacion save(Notificacion notificacion);

    /**
     * Lista notificaciones pendientes de envío.
     */
    List<Notificacion> findPendientes();

    /**
     * Lista notificaciones de un destinatario.
     */
    List<Notificacion> findByDestinatarioId(Long destinatarioId);

    /**
     * Marca una notificación como enviada.
     */
    void marcarEnviado(Long notificacionId);

    /**
     * Marca una notificación como fallida con el error indicado.
     */
    void marcarFallido(Long notificacionId, String error);
}
