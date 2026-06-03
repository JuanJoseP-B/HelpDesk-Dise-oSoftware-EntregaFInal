package com.helpdesk.domain.service;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SlaServiceTest {

    private SlaService slaService;

    @BeforeEach
    void setUp() {
        slaService = new SlaService();
    }

    @Test
    @DisplayName("Lunes 10:00 es horario hábil")
    void lunesEnHorarioHabil() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 1, 10, 0);
        assertTrue(slaService.esHorarioHabil(fecha));
    }

    @Test
    @DisplayName("Sábado no es horario hábil")
    void sabadoNoEsHorarioHabil() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 6, 10, 0);
        assertFalse(slaService.esHorarioHabil(fecha));
    }

    @Test
    @DisplayName("Fuera de jornada (20:00) no es horario hábil")
    void nocheNoEsHorarioHabil() {
        LocalDateTime fecha = LocalDateTime.of(2026, 6, 2, 20, 0);
        assertFalse(slaService.esHorarioHabil(fecha));
    }

    @Test
    @DisplayName("Suma horas hábiles saltando fin de semana")
    void sumaHorasHabilesSaltaFinDeSemana() {
        LocalDateTime viernes17 = LocalDateTime.of(2026, 6, 5, 17, 0);
        LocalDateTime resultado = slaService.calcularFechaLimiteRespuesta(viernes17, 2);
        assertEquals(LocalDateTime.of(2026, 6, 8, 10, 0), resultado);
    }

    @Test
    @DisplayName("Detecta incidencia vencida por SLA de respuesta")
    void incidenciaVencidaPorRespuesta() {
        LocalDateTime creacion = LocalDateTime.of(2026, 6, 2, 9, 0);
        Incidencia incidencia = new Incidencia(
                1L, "Título", "Desc", creacion, NivelPrioridad.ALTA, 1L, 1L
        );
        AcuerdoServicio sla = new AcuerdoServicio(
                1L, "SLA Alta", "Desc", NivelPrioridad.ALTA, 2, 8, true, creacion
        );
        LocalDateTime ahora = slaService.calcularFechaLimiteRespuesta(creacion, 2).plusMinutes(1);
        assertTrue(slaService.estaVencida(incidencia, sla, ahora));
    }

    @Test
    @DisplayName("Incidencia resuelta no está vencida")
    void resueltaNoEstaVencida() {
        LocalDateTime creacion = LocalDateTime.of(2026, 6, 2, 9, 0);
        Incidencia incidencia = new Incidencia(
                1L, "Título", "Desc", creacion, NivelPrioridad.MEDIA, 1L, 1L
        );
        incidencia.asignar(10L, creacion.plusHours(1), com.helpdesk.domain.enums.RolUsuario.ADMINISTRADOR);
        incidencia.iniciarTrabajo(com.helpdesk.domain.enums.RolUsuario.TECNICO);
        incidencia.resolver("OK", creacion.plusHours(2), com.helpdesk.domain.enums.RolUsuario.TECNICO);

        AcuerdoServicio sla = new AcuerdoServicio(
                1L, "SLA", "Desc", NivelPrioridad.MEDIA, 8, 48, true, creacion
        );
        assertFalse(slaService.estaVencida(incidencia, sla, creacion.plusDays(30)));
    }

    @Test
    @DisplayName("No permite cambiar prioridad en incidencia cerrada")
    void noCambiaPrioridadCerrada() {
        Incidencia incidencia = new Incidencia(
                1L, "T", "D", LocalDateTime.now(), NivelPrioridad.BAJA, 1L, 1L
        );
        incidencia.asignar(2L, LocalDateTime.now(), com.helpdesk.domain.enums.RolUsuario.ADMINISTRADOR);
        incidencia.iniciarTrabajo(com.helpdesk.domain.enums.RolUsuario.TECNICO);
        incidencia.resolver("s", LocalDateTime.now(), com.helpdesk.domain.enums.RolUsuario.TECNICO);
        incidencia.cerrar(LocalDateTime.now(), com.helpdesk.domain.enums.RolUsuario.CLIENTE);

        assertThrows(
                DomainException.class,
                () -> slaService.validarCambioPrioridad(incidencia, NivelPrioridad.ALTA)
        );
    }
}
