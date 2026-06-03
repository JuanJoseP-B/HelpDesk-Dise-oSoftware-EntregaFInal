package com.helpdesk.infrastructure.persistence.entity;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "incidencias",
        indexes = {
                @Index(name = "idx_incidencias_estado", columnList = "estado"),
                @Index(name = "idx_incidencias_cliente", columnList = "cliente_id"),
                @Index(name = "idx_incidencias_tecnico", columnList = "tecnico_asignado_id")
        }
)
public class IncidenciaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 4000)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoIncidencia estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_prioridad", nullable = false, length = 30)
    private NivelPrioridad nivelPrioridad;

    @Column(length = 4000)
    private String solucion;

    @Column(name = "tecnico_asignado_id")
    private Long tecnicoAsignadoId;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "acuerdo_servicio_id")
    private Long acuerdoServicioId;

    @Column(name = "sla_violado", nullable = false)
    private boolean slaViolado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(LocalDateTime fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoIncidencia estado) {
        this.estado = estado;
    }

    public NivelPrioridad getNivelPrioridad() {
        return nivelPrioridad;
    }

    public void setNivelPrioridad(NivelPrioridad nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    public Long getTecnicoAsignadoId() {
        return tecnicoAsignadoId;
    }

    public void setTecnicoAsignadoId(Long tecnicoAsignadoId) {
        this.tecnicoAsignadoId = tecnicoAsignadoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getAcuerdoServicioId() {
        return acuerdoServicioId;
    }

    public void setAcuerdoServicioId(Long acuerdoServicioId) {
        this.acuerdoServicioId = acuerdoServicioId;
    }

    public boolean isSlaViolado() {
        return slaViolado;
    }

    public void setSlaViolado(boolean slaViolado) {
        this.slaViolado = slaViolado;
    }
}
