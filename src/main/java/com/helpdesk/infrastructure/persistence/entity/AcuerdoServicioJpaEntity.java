package com.helpdesk.infrastructure.persistence.entity;

import com.helpdesk.domain.enums.NivelPrioridad;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "acuerdos_servicio",
        indexes = @Index(name = "idx_slas_prioridad", columnList = "nivel_prioridad")
)
public class AcuerdoServicioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_prioridad", nullable = false, length = 30)
    private NivelPrioridad nivelPrioridad;

    @Column(name = "tiempo_max_respuesta_horas", nullable = false)
    private int tiempoMaxRespuestaHoras;

    @Column(name = "tiempo_max_resolucion_horas", nullable = false)
    private int tiempoMaxResolucionHoras;

    @Column(nullable = false)
    private boolean activo;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public NivelPrioridad getNivelPrioridad() {
        return nivelPrioridad;
    }

    public void setNivelPrioridad(NivelPrioridad nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }

    public int getTiempoMaxRespuestaHoras() {
        return tiempoMaxRespuestaHoras;
    }

    public void setTiempoMaxRespuestaHoras(int tiempoMaxRespuestaHoras) {
        this.tiempoMaxRespuestaHoras = tiempoMaxRespuestaHoras;
    }

    public int getTiempoMaxResolucionHoras() {
        return tiempoMaxResolucionHoras;
    }

    public void setTiempoMaxResolucionHoras(int tiempoMaxResolucionHoras) {
        this.tiempoMaxResolucionHoras = tiempoMaxResolucionHoras;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
