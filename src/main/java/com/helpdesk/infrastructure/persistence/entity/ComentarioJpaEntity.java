package com.helpdesk.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios", indexes = @Index(name = "idx_comentarios_incidencia", columnList = "incidencia_id"))
public class ComentarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "incidencia_id", nullable = false)
    private Long incidenciaId;

    @Column(name = "autor_id", nullable = false)
    private Long autorId;

    @Column(nullable = false, length = 4000)
    private String contenido;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "visible_para_cliente", nullable = false)
    private boolean visibleParaCliente;

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

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isVisibleParaCliente() {
        return visibleParaCliente;
    }

    public void setVisibleParaCliente(boolean visibleParaCliente) {
        this.visibleParaCliente = visibleParaCliente;
    }
}
