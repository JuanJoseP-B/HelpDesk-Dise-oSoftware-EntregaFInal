package com.helpdesk.infrastructure.persistence.entity;

import com.helpdesk.domain.enums.EstadoIncidencia;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estados", indexes = @Index(name = "idx_historial_incidencia", columnList = "incidencia_id"))
public class HistorialEstadoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incidencia_id", nullable = false)
    private Long incidenciaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 30)
    private EstadoIncidencia estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 30)
    private EstadoIncidencia estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "usuario_cambio_id")
    private Long usuarioCambioId;

    @Column(length = 500)
    private String motivo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIncidenciaId() {
        return incidenciaId;
    }

    public void setIncidenciaId(Long incidenciaId) {
        this.incidenciaId = incidenciaId;
    }

    public EstadoIncidencia getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoIncidencia estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoIncidencia getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoIncidencia estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public Long getUsuarioCambioId() {
        return usuarioCambioId;
    }

    public void setUsuarioCambioId(Long usuarioCambioId) {
        this.usuarioCambioId = usuarioCambioId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
