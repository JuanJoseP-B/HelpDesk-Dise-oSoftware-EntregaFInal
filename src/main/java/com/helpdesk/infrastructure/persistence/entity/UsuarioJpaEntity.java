package com.helpdesk.infrastructure.persistence.entity;

import com.helpdesk.domain.enums.RolUsuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "usuarios",
        indexes = {
                @Index(name = "idx_usuarios_email", columnList = "correo_electronico", unique = true),
                @Index(name = "idx_usuarios_rol", columnList = "rol")
        }
)
public class UsuarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(name = "correo_electronico", nullable = false, unique = true, length = 180)
    private String correoElectronico;

    @Column(length = 40)
    private String telefono;

    @Column(name = "contrasena_hash", nullable = false, length = 100)
    private String contrasenaHash;

    @Column(nullable = false)
    private boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RolUsuario rol;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

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

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
}
