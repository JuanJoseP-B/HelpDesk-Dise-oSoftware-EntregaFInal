package com.helpdesk.domain.entity;

import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.exception.DomainException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Rol del sistema con conjunto de permisos configurables.
 */
public class Rol {

    private Long id;
    private RolUsuario tipo;
    private List<String> permisos;
    private String descripcion;

    public Rol() {
        this.permisos = new ArrayList<>();
    }

    public Rol(Long id, RolUsuario tipo, List<String> permisos, String descripcion) {
        this.id = id;
        this.tipo = tipo;
        this.permisos = permisos != null ? new ArrayList<>(permisos) : new ArrayList<>();
        this.descripcion = descripcion;
        if (tipo != null && this.permisos.isEmpty()) {
            this.permisos.addAll(tipo.getPermisos());
        }
    }

    /**
     * Otorga un permiso al rol si aún no lo tiene.
     */
    public void otorgarPermiso(String permiso) {
        validarPermiso(permiso);
        if (!permisos.contains(permiso)) {
            permisos.add(permiso);
        }
    }

    /**
     * Revoca un permiso del rol.
     */
    public void revocarPermiso(String permiso) {
        validarPermiso(permiso);
        permisos.remove(permiso);
    }

    /**
     * Indica si el rol posee el permiso indicado.
     */
    public boolean tienePermiso(String permiso) {
        return permisos.contains(permiso);
    }

    private void validarPermiso(String permiso) {
        if (permiso == null || permiso.isBlank()) {
            throw new DomainException("El permiso no puede estar vacío");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RolUsuario getTipo() {
        return tipo;
    }

    public List<String> getPermisos() {
        return Collections.unmodifiableList(permisos);
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rol rol = (Rol) o;
        return Objects.equals(id, rol.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
