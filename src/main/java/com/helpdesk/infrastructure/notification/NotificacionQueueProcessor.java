package com.helpdesk.infrastructure.notification;

import com.helpdesk.domain.entity.Notificacion;
import com.helpdesk.domain.port.NotificationPort;
import com.helpdesk.domain.repository.NotificacionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Procesa periodicamente notificaciones pendientes.
 */
@Component
public class NotificacionQueueProcessor {

    private final NotificacionRepository notificacionRepository;
    private final NotificationPort notificationPort;

    public NotificacionQueueProcessor(NotificacionRepository notificacionRepository, NotificationPort notificationPort) {
        this.notificacionRepository = notificacionRepository;
        this.notificationPort = notificationPort;
    }

    @Scheduled(fixedDelayString = "${app.notifications.queue-delay-ms:300000}")
    @Transactional
    public void procesarPendientes() {
        for (Notificacion notificacion : notificacionRepository.findPendientes()) {
            try {
                notificationPort.enviar(notificacion);
                notificacionRepository.marcarEnviado(notificacion.getId());
            } catch (RuntimeException ex) {
                notificacionRepository.marcarFallido(notificacion.getId(), ex.getMessage());
            }
        }
    }
}
