package com.helpdesk.infrastructure.persistence.entity;

import com.helpdesk.domain.enums.TipoNotificacion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notificaciones",
        indexes = {
                @Index(name = "idx_notificaciones_destinatario", columnList = "destinatario_id"),
                @Index(name = "idx_notificaciones_estado", columnList = "estado_envio")
        }
)
public class NotificacionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "destinatario_id", nullable = false)
    private Long destinatarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TipoNotificacion tipo;

    @Column(nullable = false, length = 180)
    private String asunto;

    @Column(nullable = false, length = 4000)
    private String mensaje;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "estado_envio", nullable = false, length = 30)
    private String estadoEnvio;

    @Column(nullable = false)
    private boolean leida;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
