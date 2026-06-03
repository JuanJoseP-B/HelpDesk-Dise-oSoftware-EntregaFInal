package com.helpdesk.infrastructure.event;

import com.helpdesk.domain.port.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publicador de eventos de dominio hacia el bus de eventos de Spring.
 */
@Component
public class SpringEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publicar(Object evento) {
        publisher.publishEvent(evento);
    }
}
