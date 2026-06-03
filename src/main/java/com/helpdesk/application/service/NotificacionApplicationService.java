package com.helpdesk.application.service;

import com.helpdesk.domain.entity.Notificacion;
import com.helpdesk.domain.enums.TipoNotificacion;
import com.helpdesk.domain.event.*;
import com.helpdesk.domain.repository.NotificacionRepository;
import org.springframework.stereotype.Service;

/**
 * Orquesta la creación de notificaciones a partir de eventos de dominio.
 */
@Service
public class NotificacionApplicationService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionApplicationService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * Registra notificación por creación de ticket.
     */
    public void onTicketCreado(TicketCreadoEvent evento) {
        guardar(evento.getClienteId(), TipoNotificacion.CREACION_TICKET,
                "Ticket creado #" + evento.getTicketId(),
                "Su incidencia fue registrada con prioridad " + evento.getPrioridad());
    }

    /**
     * Registra notificación por asignación de ticket.
     */
    public void onTicketAsignado(TicketAsignadoEvent evento) {
        guardar(evento.getTecnicoId(), TipoNotificacion.ASIGNACION,
                "Ticket asignado #" + evento.getTicketId(),
                "Se le ha asignado una nueva incidencia");
    }

    /**
     * Registra notificación por cambio de estado.
     */
    public void onEstadoCambiado(EstadoCambiadoEvent evento) {
        guardar(evento.getUsuarioId(), TipoNotificacion.CAMBIO_ESTADO,
                "Cambio de estado #" + evento.getTicketId(),
                "Estado: " + evento.getEstadoAnterior() + " → " + evento.getEstadoNuevo());
    }

    /**
     * Registra notificación por resolución.
     */
    public void onTicketResuelto(TicketResueltoEvent evento) {
        guardar(evento.getTecnicoId(), TipoNotificacion.RESOLUCION,
                "Ticket resuelto #" + evento.getTicketId(),
                "Incidencia resuelta: " + evento.getSolucion());
    }

    /**
     * Registra alerta por violación de SLA (destinatario admin se resuelve en infraestructura).
     */
    public void onSlaViolado(SlaVioladoEvent evento, Long adminId) {
        guardar(adminId, TipoNotificacion.SLA_VIOLADO,
                "SLA violado #" + evento.getTicketId(),
                "Tipo SLA: " + evento.getTipoSla() + ", prioridad " + evento.getPrioridad());
    }

    /**
     * Registra notificación de nuevo comentario.
     */
    public void onComentarioNuevo(Long destinatarioId, Long incidenciaId) {
        guardar(destinatarioId, TipoNotificacion.COMENTARIO_NUEVO,
                "Nuevo comentario #" + incidenciaId,
                "Hay un nuevo comentario en su incidencia");
    }

    private void guardar(Long destinatarioId, TipoNotificacion tipo, String asunto, String mensaje) {
        Notificacion notificacion = new Notificacion(
                null,
                destinatarioId,
                tipo,
                asunto,
                mensaje,
                null,
                Notificacion.ESTADO_PENDIENTE,
                false
        );
        notificacionRepository.save(notificacion);
    }
}
