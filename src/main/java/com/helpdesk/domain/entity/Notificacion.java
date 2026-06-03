package com.helpdesk.domain.entity;

import com.helpdesk.domain.enums.TipoNotificacion;
import com.helpdesk.domain.exception.DomainException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Notificación enviada a un usuario del sistema.
 */
public class Notificacion {

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_ENVIADO = "ENVIADO";
    public static final String ESTADO_FALLIDO = "FALLIDO";

    private Long id;
    private Long destinatarioId;
    private TipoNotificacion tipo;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String estadoEnvio;
    private boolean leida;

    public Notificacion() {
        this.estadoEnvio = ESTADO_PENDIENTE;
        this.leida = false;
    }

    public Notificacion(
            Long id,
            Long destinatarioId,
            TipoNotificacion tipo,
            String asunto,
            String mensaje,
            LocalDateTime fechaEnvio,
            String estadoEnvio,
            boolean leida
    ) {
        this.id = id;
        this.destinatarioId = destinatarioId;
        this.tipo = tipo;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.estadoEnvio = estadoEnvio;
        this.leida = leida;
    }

    /**
     * Marca la notificación como enviada correctamente.
     */
    public void marcarEnviado() {
        this.estadoEnvio = ESTADO_ENVIADO;
        this.fechaEnvio = LocalDateTime.now();
    }

    /**
     * Marca la notificación como fallida e incluye el error.
     */
    public void marcarFallido(String error) {
        if (error == null || error.isBlank()) {
            throw new DomainException("El mensaje de error es obligatorio al marcar fallo");
        }
        this.estadoEnvio = ESTADO_FALLIDO;
        this.mensaje = this.mensaje + " [Error: " + error + "]";
    }

    /**
     * Marca la notificación como leída por el destinatario.
     */
    public void marcarLeida() {
        this.leida = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public boolean isLeida() {
        return leida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notificacion that = (Notificacion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
