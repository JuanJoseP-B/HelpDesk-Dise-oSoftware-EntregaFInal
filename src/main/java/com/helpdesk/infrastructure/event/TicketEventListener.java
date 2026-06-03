package com.helpdesk.infrastructure.event;

import com.helpdesk.application.service.NotificacionApplicationService;
import com.helpdesk.domain.event.TicketAsignadoEvent;
import com.helpdesk.domain.event.TicketCreadoEvent;
import com.helpdesk.domain.event.TicketResueltoEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener de eventos de tickets para registrar notificaciones.
 */
@Component
public class TicketEventListener {

    private final NotificacionApplicationService notificacionApplicationService;

    public TicketEventListener(NotificacionApplicationService notificacionApplicationService) {
        this.notificacionApplicationService = notificacionApplicationService;
    }

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTicketCreado(TicketCreadoEvent event) {
        notificacionApplicationService.onTicketCreado(event);
    }

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTicketAsignado(TicketAsignadoEvent event) {
        notificacionApplicationService.onTicketAsignado(event);
    }

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTicketResuelto(TicketResueltoEvent event) {
        notificacionApplicationService.onTicketResuelto(event);
    }
}
