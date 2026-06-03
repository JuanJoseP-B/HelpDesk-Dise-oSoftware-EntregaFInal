package com.helpdesk.infrastructure.event;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.event.SlaVioladoEvent;
import com.helpdesk.domain.port.DomainEventPublisher;
import com.helpdesk.domain.repository.AcuerdoServicioRepository;
import com.helpdesk.domain.repository.IncidenciaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class SlaMonitorSchedulerIT {

    @Autowired
    private SlaMonitorScheduler scheduler;

    @MockBean
    private IncidenciaRepository incidenciaRepository;

    @MockBean
    private AcuerdoServicioRepository acuerdoServicioRepository;

    @MockBean
    private DomainEventPublisher eventPublisher;

    @Test
    void schedulerMarcaSlaVioladoYPublicaEvento() {
        Incidencia incidencia = new Incidencia(
                10L,
                "SLA vencido",
                "Ticket antiguo",
                LocalDateTime.now().minusDays(10),
                NivelPrioridad.ALTA,
                3L,
                1L
        );
        AcuerdoServicio acuerdo = new AcuerdoServicio(
                1L,
                "SLA Alta",
                "Resolucion alta",
                NivelPrioridad.ALTA,
                2,
                8,
                true,
                LocalDateTime.now().minusDays(20)
        );

        when(incidenciaRepository.findVencidas(any(LocalDateTime.class))).thenReturn(List.of(incidencia));
        when(acuerdoServicioRepository.findById(1L)).thenReturn(Optional.of(acuerdo));
        when(incidenciaRepository.save(any(Incidencia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        scheduler.verificarSla();

        verify(incidenciaRepository).save(incidencia);
        verify(eventPublisher).publicar(any(SlaVioladoEvent.class));
    }
}
