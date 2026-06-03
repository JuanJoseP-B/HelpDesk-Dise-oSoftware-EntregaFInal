package com.helpdesk.domain.entity;

import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Acuerdo de nivel de servicio (SLA) asociado a una prioridad.
 */
public class AcuerdoServicio {

    private Long id;
    private String nombre;
    private String descripcion;
    private NivelPrioridad nivelPrioridad;
    private int tiempoMaxRespuestaHoras;
    private int tiempoMaxResolucionHoras;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    public AcuerdoServicio() {
        this.activo = true;
    }

    public AcuerdoServicio(
            Long id,
            String nombre,
            String descripcion,
            NivelPrioridad nivelPrioridad,
            int tiempoMaxRespuestaHoras,
            int tiempoMaxResolucionHoras,
            boolean activo,
            LocalDateTime fechaCreacion
    ) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.nivelPrioridad = nivelPrioridad;
        this.tiempoMaxRespuestaHoras = tiempoMaxRespuestaHoras;
        this.tiempoMaxResolucionHoras = tiempoMaxResolucionHoras;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Calcula la fecha límite de primera respuesta a partir del instante de inicio.
     */
    public LocalDateTime calcularFechaLimiteRespuesta(LocalDateTime inicio) {
        validarInicio(inicio);
        return inicio.plusHours(tiempoMaxRespuestaHoras);
    }

    /**
     * Calcula la fecha límite de resolución a partir del instante de inicio.
     */
    public LocalDateTime calcularFechaLimiteResolucion(LocalDateTime inicio) {
        validarInicio(inicio);
        return inicio.plusHours(tiempoMaxResolucionHoras);
    }

    /**
     * Indica si el acuerdo está activo y puede aplicarse.
     */
    public boolean estaActivo() {
        return activo;
    }

    private void validarInicio(LocalDateTime inicio) {
        if (inicio == null) {
            throw new DomainException("La fecha de inicio es obligatoria para calcular límites SLA");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public NivelPrioridad getNivelPrioridad() {
        return nivelPrioridad;
    }

    public int getTiempoMaxRespuestaHoras() {
        return tiempoMaxRespuestaHoras;
    }

    public int getTiempoMaxResolucionHoras() {
        return tiempoMaxResolucionHoras;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcuerdoServicio that = (AcuerdoServicio) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
