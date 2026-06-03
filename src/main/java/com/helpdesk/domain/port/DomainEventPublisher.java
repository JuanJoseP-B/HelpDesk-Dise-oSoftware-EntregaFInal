package com.helpdesk.domain.port;

/**
 * Puerto de salida para publicar eventos de dominio.
 */
public interface DomainEventPublisher {

    /**
     * Publica un evento de dominio a los suscriptores configurados.
     *
     * @param evento evento inmutable de dominio
     */
    void publicar(Object evento);
}
