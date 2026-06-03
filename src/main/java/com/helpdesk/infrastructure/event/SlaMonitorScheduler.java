package com.helpdesk.infrastructure.event;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.event.SlaVioladoEvent;
import com.helpdesk.domain.port.DomainEventPublisher;
import com.helpdesk.domain.repository.AcuerdoServicioRepository;
import com.helpdesk.domain.repository.IncidenciaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Verifica incidencias vencidas y emite eventos SLA.
 */
@Component
public class SlaMonitorScheduler {

    private final IncidenciaRepository incidenciaRepository;
    private final AcuerdoServicioRepository acuerdoServicioRepository;
    private final DomainEventPublisher eventPublisher;

    public SlaMonitorScheduler(
            IncidenciaRepository incidenciaRepository,
            AcuerdoServicioRepository acuerdoServicioRepository,
            DomainEventPublisher eventPublisher
    ) {
        this.incidenciaRepository = incidenciaRepository;
        this.acuerdoServicioRepository = acuerdoServicioRepository;
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedDelayString = "${app.sla.check-delay-ms:300000}")
    @Transactional
    public void verificarSla() {
        LocalDateTime ahora = LocalDateTime.now();
        for (Incidencia incidencia : incidenciaRepository.findVencidas(ahora)) {
            acuerdoServicioRepository.findById(incidencia.getAcuerdoServicioId())
                    .filter(acuerdo -> incidencia.estaVencida(ahora, acuerdo))
                    .ifPresent(acuerdo -> marcarYPublicar(incidencia, acuerdo, ahora));
        }
    }

    private void marcarYPublicar(Incidencia incidencia, AcuerdoServicio acuerdo, LocalDateTime ahora) {
        incidencia.marcarSlaViolado();
        incidenciaRepository.save(incidencia);
        eventPublisher.publicar(new SlaVioladoEvent(
                incidencia.getId(),
                incidencia.getNivelPrioridad(),
                acuerdo.calcularFechaLimiteResolucion(incidencia.getFechaCreacion()),
                "RESOLUCION"
        ));
    }
}
