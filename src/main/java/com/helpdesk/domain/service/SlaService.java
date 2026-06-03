package com.helpdesk.domain.service;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.exception.DomainException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Servicio de dominio para cálculo y validación de acuerdos de nivel de servicio (SLA).
 * Las horas SLA se computan en horario hábil: lunes a viernes, 08:00–18:00.
 */
public class SlaService {

    private static final LocalTime HORA_INICIO_JORNADA = LocalTime.of(8, 0);
    private static final LocalTime HORA_FIN_JORNADA = LocalTime.of(18, 0);

    /**
     * Indica si la incidencia ha superado el SLA según el acuerdo y el instante de referencia.
     */
    public boolean estaVencida(Incidencia incidencia, AcuerdoServicio acuerdo, LocalDateTime ahora) {
        Objects.requireNonNull(incidencia, "incidencia");
        Objects.requireNonNull(acuerdo, "acuerdo");
        Objects.requireNonNull(ahora, "ahora");

        if (!acuerdo.estaActivo() || incidencia.getFechaCreacion() == null) {
            return false;
        }

        EstadoIncidencia estado = incidencia.getEstado();
        if (estado == EstadoIncidencia.CERRADO || estado == EstadoIncidencia.RESUELTO) {
            return false;
        }

        LocalDateTime limiteRespuesta = calcularFechaLimiteRespuesta(
                incidencia.getFechaCreacion(),
                acuerdo.getTiempoMaxRespuestaHoras()
        );

        if (estado == EstadoIncidencia.ABIERTO && ahora.isAfter(limiteRespuesta)) {
            return true;
        }

        LocalDateTime limiteResolucion = calcularFechaLimiteResolucion(
                incidencia.getFechaCreacion(),
                acuerdo.getTiempoMaxResolucionHoras()
        );
        return ahora.isAfter(limiteResolucion);
    }

    /**
     * Calcula la fecha límite de primera respuesta sumando horas hábiles desde el inicio.
     */
    public LocalDateTime calcularFechaLimiteRespuesta(LocalDateTime inicio, int horas) {
        return sumarHorasHabiles(inicio, horas);
    }

    /**
     * Calcula la fecha límite de resolución sumando horas hábiles desde el inicio.
     */
    public LocalDateTime calcularFechaLimiteResolucion(LocalDateTime inicio, int horas) {
        return sumarHorasHabiles(inicio, horas);
    }

    /**
     * Indica si la fecha cae en horario hábil (L–V, 08:00–18:00).
     */
    public boolean esHorarioHabil(LocalDateTime fecha) {
        Objects.requireNonNull(fecha, "fecha");
        if (esFinDeSemana(fecha.getDayOfWeek())) {
            return false;
        }
        LocalTime hora = fecha.toLocalTime();
        return !hora.isBefore(HORA_INICIO_JORNADA) && hora.isBefore(HORA_FIN_JORNADA);
    }

    /**
     * Valida que el cambio de prioridad sea coherente con el estado de la incidencia.
     */
    public void validarCambioPrioridad(Incidencia incidencia, NivelPrioridad nueva) {
        Objects.requireNonNull(incidencia, "incidencia");
        if (nueva == null) {
            throw new DomainException("La nueva prioridad es obligatoria");
        }
        if (incidencia.getEstado() == EstadoIncidencia.CERRADO) {
            throw new DomainException("No se puede cambiar la prioridad de una incidencia cerrada");
        }
        if (incidencia.getNivelPrioridad() == nueva) {
            throw new DomainException("La incidencia ya tiene la prioridad " + nueva);
        }
    }

    /**
     * Suma horas hábiles a partir de un instante inicial.
     */
    LocalDateTime sumarHorasHabiles(LocalDateTime inicio, int horas) {
        if (inicio == null) {
            throw new DomainException("La fecha de inicio es obligatoria");
        }
        if (horas < 0) {
            throw new DomainException("Las horas no pueden ser negativas");
        }
        if (horas == 0) {
            return normalizarAHorarioHabil(inicio);
        }

        LocalDateTime cursor = normalizarAHorarioHabil(inicio);
        int horasAgregadas = 0;

        while (horasAgregadas < horas) {
            cursor = cursor.plusHours(1);
            if (esHorarioHabil(cursor)) {
                horasAgregadas++;
            } else {
                cursor = normalizarAHorarioHabil(cursor);
            }
        }
        return cursor;
    }

    private LocalDateTime normalizarAHorarioHabil(LocalDateTime fecha) {
        LocalDateTime cursor = fecha;

        while (!esHorarioHabil(cursor)) {
            if (!esFinDeSemana(cursor.getDayOfWeek())
                    && cursor.toLocalTime().isBefore(HORA_INICIO_JORNADA)) {
                return cursor.with(HORA_INICIO_JORNADA);
            }
            cursor = avanzarAlSiguienteDiaLaboral(cursor);
        }
        return cursor;
    }

    private LocalDateTime avanzarAlSiguienteDiaLaboral(LocalDateTime fecha) {
        LocalDateTime siguiente = fecha.toLocalDate().plusDays(1).atTime(HORA_INICIO_JORNADA);
        while (esFinDeSemana(siguiente.getDayOfWeek())) {
            siguiente = siguiente.plusDays(1);
        }
        return siguiente;
    }

    private boolean esFinDeSemana(DayOfWeek dia) {
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }
}
