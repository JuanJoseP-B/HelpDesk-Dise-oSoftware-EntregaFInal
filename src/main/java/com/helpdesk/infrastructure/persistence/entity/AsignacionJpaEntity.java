package com.helpdesk.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "asignaciones",
        indexes = {
                @Index(name = "idx_asignaciones_incidencia", columnList = "incidencia_id"),
                @Index(name = "idx_asignaciones_tecnico", columnList = "tecnico_id")
        }
)
public class AsignacionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incidencia_id", nullable = false)
    private Long incidenciaId;

    @Column(name = "tecnico_id", nullable = false)
    private Long tecnicoId;

    @Column(name = "asignado_por_id", nullable = false)
    private Long asignadoPorId;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(length = 500)
    private String motivo;

    @Column(nullable = false)
    private boolean activa;

    @Column(nullable = false)
    private boolean automatica;

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

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public Long getAsignadoPorId() {
        return asignadoPorId;
    }

    public void setAsignadoPorId(Long asignadoPorId) {
        this.asignadoPorId = asignadoPorId;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isAutomatica() {
        return automatica;
    }

    public void setAutomatica(boolean automatica) {
        this.automatica = automatica;
    }
}
