package com.helpdesk.domain.entity;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.exception.TransicionEstadoInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests unitarios de transiciones de estado de {@link Incidencia}.
 */
class IncidenciaTest {

    private Incidencia incidencia;
    private LocalDateTime ahora;

    @BeforeEach
    void setUp() {
        ahora = LocalDateTime.of(2026, 6, 3, 10, 0);
        incidencia = new Incidencia(
                1L,
                "Impresora no funciona",
                "La impresora del piso 2 no imprime",
                ahora,
                NivelPrioridad.MEDIA,
                100L,
                1L
        );
    }

    @Test
    @DisplayName("Flujo completo válido: ABIERTO → ASIGNADO → EN_PROGRESO → RESUELTO → CERRADO")
    void flujoCompletoValido() {
        incidencia.asignar(200L, ahora.plusHours(1), RolUsuario.ADMINISTRADOR);
        assertEquals(EstadoIncidencia.ASIGNADO, incidencia.getEstado());

        incidencia.iniciarTrabajo(RolUsuario.TECNICO);
        assertEquals(EstadoIncidencia.EN_PROGRESO, incidencia.getEstado());

        incidencia.resolver("Reinicio de spooler", ahora.plusHours(4), RolUsuario.TECNICO);
        assertEquals(EstadoIncidencia.RESUELTO, incidencia.getEstado());

        incidencia.cerrar(ahora.plusHours(5), RolUsuario.CLIENTE);
        assertEquals(EstadoIncidencia.CERRADO, incidencia.getEstado());
        assertEquals(4, incidencia.getHistorial().size());
    }

    @Test
    @DisplayName("No permite iniciar trabajo desde ABIERTO")
    void noPermiteIniciarDesdeAbierto() {
        assertThrows(
                TransicionEstadoInvalidaException.class,
                () -> incidencia.iniciarTrabajo(RolUsuario.TECNICO)
        );
    }

    @Test
    @DisplayName("No permite cerrar desde ABIERTO sin pasar por RESUELTO")
    void noPermiteCerrarDesdeAbierto() {
        assertThrows(
                TransicionEstadoInvalidaException.class,
                () -> incidencia.cerrar(ahora, RolUsuario.CLIENTE)
        );
    }

    @Test
    @DisplayName("Cliente puede reabrir dentro de 30 días")
    void clienteReabreDentroDePlazo() {
        completarHastaCerrado();
        incidencia.reabrir("Problema persiste", RolUsuario.CLIENTE);
        assertEquals(EstadoIncidencia.ABIERTO, incidencia.getEstado());
        assertFalse(incidencia.isSlaViolado());
    }

    @Test
    @DisplayName("Cliente no puede reabrir después de 30 días")
    void clienteNoReabreFueraDePlazo() {
        completarHastaCerrado();
        incidencia = new Incidencia(
                1L,
                "Impresora no funciona",
                "Descripción",
                ahora.minusDays(60),
                NivelPrioridad.MEDIA,
                100L,
                1L
        );
        incidencia.asignar(200L, ahora.minusDays(59), RolUsuario.ADMINISTRADOR);
        incidencia.iniciarTrabajo(RolUsuario.TECNICO);
        incidencia.resolver("Solución", ahora.minusDays(58), RolUsuario.TECNICO);
        incidencia.cerrar(ahora.minusDays(31), RolUsuario.CLIENTE);

        assertThrows(
                TransicionEstadoInvalidaException.class,
                () -> incidencia.reabrir("Tarde", RolUsuario.CLIENTE)
        );
    }

    @Test
    @DisplayName("Administrador puede saltar estados hacia adelante")
    void adminSaltaEstados() {
        incidencia.resolver("Cierre rápido admin", ahora.plusHours(1), RolUsuario.ADMINISTRADOR);
        assertEquals(EstadoIncidencia.RESUELTO, incidencia.getEstado());
    }

    @Test
    @DisplayName("Re-asignación permitida en ASIGNADO y EN_PROGRESO")
    void reasignacionPermitida() {
        incidencia.asignar(200L, ahora, RolUsuario.ADMINISTRADOR);
        incidencia.asignar(201L, ahora.plusHours(1), RolUsuario.ADMINISTRADOR);
        assertEquals(201L, incidencia.getTecnicoAsignadoId());

        incidencia.iniciarTrabajo(RolUsuario.TECNICO);
        incidencia.asignar(202L, ahora.plusHours(2), RolUsuario.ADMINISTRADOR);
        assertEquals(202L, incidencia.getTecnicoAsignadoId());
        assertEquals(EstadoIncidencia.EN_PROGRESO, incidencia.getEstado());
    }

    @Test
    @DisplayName("Detecta incidencia vencida según SLA")
    void detectaIncidenciaVencida() {
        AcuerdoServicio sla = new AcuerdoServicio(
                1L,
                "SLA Media",
                "Acuerdo media",
                NivelPrioridad.MEDIA,
                8,
                48,
                true,
                ahora
        );
        assertTrue(incidencia.estaVencida(ahora.plusHours(9), sla));
        assertFalse(incidencia.estaVencida(ahora.plusHours(2), sla));
    }

    @Test
    @DisplayName("Marcar SLA violado actualiza bandera")
    void marcarSlaViolado() {
        incidencia.marcarSlaViolado();
        assertTrue(incidencia.isSlaViolado());
    }

    private void completarHastaCerrado() {
        incidencia.asignar(200L, ahora, RolUsuario.ADMINISTRADOR);
        incidencia.iniciarTrabajo(RolUsuario.TECNICO);
        incidencia.resolver("Solución aplicada", ahora.plusHours(2), RolUsuario.TECNICO);
        incidencia.cerrar(ahora.plusHours(3), RolUsuario.CLIENTE);
    }
}
