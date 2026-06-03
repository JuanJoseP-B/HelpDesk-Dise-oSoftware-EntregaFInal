package com.helpdesk.domain.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro de asignación de una incidencia a un técnico.
 */
public class Asignacion {

    private Long id;
    private Long incidenciaId;
    private Long tecnicoId;
    private Long asignadoPorId;
    private LocalDateTime fechaAsignacion;
    private String motivo;
    private boolean activa;
    private boolean automatica;

    public Asignacion() {
        this.activa = true;
    }

    public Asignacion(
            Long id,
            Long incidenciaId,
            Long tecnicoId,
            Long asignadoPorId,
            LocalDateTime fechaAsignacion,
            String motivo,
            boolean activa,
            boolean automatica
    ) {
        this.id = id;
        this.incidenciaId = incidenciaId;
        this.tecnicoId = tecnicoId;
        this.asignadoPorId = asignadoPorId;
        this.fechaAsignacion = fechaAsignacion;
        this.motivo = motivo;
        this.activa = activa;
        this.automatica = automatica;
    }

    /**
     * Desactiva la asignación actual (por re-asignación o cierre).
     */
    public void desactivar() {
        this.activa = false;
    }

    /**
     * Indica si la asignación fue generada automáticamente por el sistema.
     */
    public boolean esAutomatica() {
        return automatica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIncidenciaId() {
        return incidenciaId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public Long getAsignadoPorId() {
        return asignadoPorId;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean isActiva() {
        return activa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Asignacion that = (Asignacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
