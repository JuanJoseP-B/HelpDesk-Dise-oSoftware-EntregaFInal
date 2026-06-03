package com.helpdesk.infrastructure.persistence.entity;

import com.helpdesk.domain.enums.RolUsuario;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles", indexes = @Index(name = "idx_roles_tipo", columnList = "tipo", unique = true))
public class RolJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RolUsuario tipo;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "rol_permisos", joinColumns = @JoinColumn(name = "rol_id"))
    @Column(name = "permiso", nullable = false, length = 80)
    private List<String> permisos = new ArrayList<>();

    @Column(length = 500)
    private String descripcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RolUsuario getTipo() {
        return tipo;
    }

    public void setTipo(RolUsuario tipo) {
        this.tipo = tipo;
    }

    public List<String> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
