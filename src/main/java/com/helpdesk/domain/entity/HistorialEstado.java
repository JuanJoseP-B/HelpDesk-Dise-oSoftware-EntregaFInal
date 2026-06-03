package com.helpdesk.domain.entity;

import com.helpdesk.domain.enums.EstadoIncidencia;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro de auditoría de cambios de estado de una incidencia.
 */
public class HistorialEstado {

    private Long id;
    private Long incidenciaId;
    private EstadoIncidencia estadoAnterior;
    private EstadoIncidencia estadoNuevo;
    private LocalDateTime fechaCambio;
    private Long usuarioCambioId;
    private String motivo;

    public HistorialEstado() {
    }

    public HistorialEstado(
            Long id,
            Long incidenciaId,
            EstadoIncidencia estadoAnterior,
            EstadoIncidencia estadoNuevo,
            LocalDateTime fechaCambio,
            Long usuarioCambioId,
            String motivo
    ) {
        this.id = id;
        this.incidenciaId = incidenciaId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
        this.usuarioCambioId = usuarioCambioId;
        this.motivo = motivo;
    }

    /**
     * Factory para crear un registro de historial con la fecha actual.
     */
    public static HistorialEstado crear(
            Long incidenciaId,
            EstadoIncidencia anterior,
            EstadoIncidencia nuevo,
            Long usuarioId,
            String motivo
    ) {
        return new HistorialEstado(
                null,
                incidenciaId,
                anterior,
                nuevo,
                LocalDateTime.now(),
                usuarioId,
                motivo
        );
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

    public EstadoIncidencia getEstadoAnterior() {
        return estadoAnterior;
    }

    public EstadoIncidencia getEstadoNuevo() {
        return estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public Long getUsuarioCambioId() {
        return usuarioCambioId;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HistorialEstado that = (HistorialEstado) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
