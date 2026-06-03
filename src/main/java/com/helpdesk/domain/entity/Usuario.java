package com.helpdesk.domain.entity;

import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.exception.DomainException;
import com.helpdesk.domain.exception.UsuarioNoAutorizadoException;
import com.helpdesk.domain.port.VerificadorContrasena;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Usuario del sistema HelpDesk con credenciales y rol.
 */
public class Usuario {

    private Long id;
    private String nombre;
    private String correoElectronico;
    private String telefono;
    private String contrasenaHash;
    private boolean activo;
    private RolUsuario rol;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;

    public Usuario() {
        this.activo = true;
    }

    public Usuario(
            Long id,
            String nombre,
            String correoElectronico,
            String telefono,
            String contrasenaHash,
            boolean activo,
            RolUsuario rol,
            LocalDateTime fechaRegistro,
            LocalDateTime ultimoAcceso
    ) {
        this.id = id;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.contrasenaHash = contrasenaHash;
        this.activo = activo;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.ultimoAcceso = ultimoAcceso;
    }

    /**
     * Activa la cuenta del usuario.
     */
    public void activar() {
        this.activo = true;
    }

    /**
     * Desactiva la cuenta del usuario.
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Actualiza la fecha del último acceso al sistema.
     */
    public void actualizarUltimoAcceso(LocalDateTime fecha) {
        if (fecha == null) {
            throw new DomainException("La fecha de último acceso es obligatoria");
        }
        this.ultimoAcceso = fecha;
    }

    /**
     * Verifica si el rol del usuario incluye el permiso indicado.
     */
    public boolean tienePermiso(String permiso) {
        if (rol == null) {
            return false;
        }
        return rol.tienePermiso(permiso);
    }

    /**
     * Valida las credenciales del usuario contra el verificador de contraseñas.
     */
    public boolean validarCredenciales(String password, VerificadorContrasena verificador) {
        if (!activo) {
            return false;
        }
        if (password == null || contrasenaHash == null || verificador == null) {
            return false;
        }
        return verificador.coincide(password, contrasenaHash);
    }

    /**
     * Actualiza nombre y/o teléfono del perfil.
     */
    public void actualizarPerfil(String nombre, String telefono) {
        if (nombre != null && !nombre.isBlank()) {
            this.nombre = nombre.trim();
        }
        if (telefono != null) {
            this.telefono = telefono;
        }
    }

    /**
     * Cambia el rol del usuario (operación administrativa).
     */
    public void cambiarRol(RolUsuario nuevoRol) {
        if (nuevoRol == null) {
            throw new DomainException("El nuevo rol es obligatorio");
        }
        this.rol = nuevoRol;
    }

    /**
     * Exige que el usuario tenga el permiso indicado; lanza excepción si no lo tiene.
     */
    public void exigirPermiso(String permiso) {
        if (!tienePermiso(permiso)) {
            throw new UsuarioNoAutorizadoException(permiso);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public boolean isActivo() {
        return activo;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
