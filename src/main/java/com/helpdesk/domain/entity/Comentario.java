package com.helpdesk.domain.entity;

import com.helpdesk.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Comentario asociado a una incidencia.
 */
public class Comentario {

    private Long id;
    private Long incidenciaId;
    private Long autorId;
    private String contenido;
    private LocalDateTime fechaHora;
    private boolean visibleParaCliente;

    public Comentario() {
    }

    public Comentario(
            Long id,
            Long incidenciaId,
            Long autorId,
            String contenido,
            LocalDateTime fechaHora,
            boolean visibleParaCliente
    ) {
        this.id = id;
        this.incidenciaId = incidenciaId;
        this.autorId = autorId;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
        this.visibleParaCliente = visibleParaCliente;
    }

    /**
     * Actualiza el contenido del comentario validando que no esté vacío.
     */
    public void editar(String nuevoContenido) {
        if (nuevoContenido == null || nuevoContenido.isBlank()) {
            throw new DomainException("El contenido del comentario no puede estar vacío");
        }
        this.contenido = nuevoContenido.trim();
    }

    /**
     * Oculta el comentario para el cliente final.
     */
    public void ocultarParaCliente() {
        this.visibleParaCliente = false;
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

    public Long getAutorId() {
        return autorId;
    }

    public String getContenido() {
        return contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public boolean isVisibleParaCliente() {
        return visibleParaCliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comentario that = (Comentario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
