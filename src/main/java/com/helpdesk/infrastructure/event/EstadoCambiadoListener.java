package com.helpdesk.infrastructure.event;

import com.helpdesk.application.service.NotificacionApplicationService;
import com.helpdesk.domain.entity.HistorialEstado;
import com.helpdesk.domain.event.EstadoCambiadoEvent;
import com.helpdesk.domain.repository.HistorialEstadoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener de cambios de estado para historial y notificaciones.
 */
@Component
public class EstadoCambiadoListener {

    private final HistorialEstadoRepository historialEstadoRepository;
    private final NotificacionApplicationService notificacionApplicationService;

    public EstadoCambiadoListener(
            HistorialEstadoRepository historialEstadoRepository,
            NotificacionApplicationService notificacionApplicationService
    ) {
        this.historialEstadoRepository = historialEstadoRepository;
        this.notificacionApplicationService = notificacionApplicationService;
    }

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEstadoCambiado(EstadoCambiadoEvent event) {
        historialEstadoRepository.save(new HistorialEstado(
                null,
                event.getTicketId(),
                event.getEstadoAnterior(),
                event.getEstadoNuevo(),
                event.getFechaCambio(),
                event.getUsuarioId(),
                event.getMotivo()
        ));
        notificacionApplicationService.onEstadoCambiado(event);
    }
}
