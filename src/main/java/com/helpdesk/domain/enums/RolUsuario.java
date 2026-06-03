package com.helpdesk.domain.enums;

import java.util.Collections;
import java.util.List;

/**
 * Roles del sistema con permisos asociados.
 */
public enum RolUsuario {
    CLIENTE(List.of(
            "CREAR_INCIDENCIA",
            "VER_MIS_TICKETS",
            "CERRAR_TICKET",
            "REABRIR_TICKET",
            "COMENTAR"
    )),
    TECNICO(List.of(
            "VER_ASIGNADAS",
            "INICIAR_TRABAJO",
            "RESOLVER_TICKET",
            "COMENTAR",
            "COMENTAR_INTERNO"
    )),
    ADMINISTRADOR(List.of(
            "CREAR_INCIDENCIA",
            "VER_TODAS",
            "ASIGNAR_TICKET",
            "CAMBIAR_PRIORIDAD",
            "GESTIONAR_USUARIOS",
            "GESTIONAR_SLA",
            "VER_REPORTES",
            "SALTAR_ESTADOS",
            "CERRAR_TICKET",
            "COMENTAR",
            "COMENTAR_INTERNO"
    ));

    private final List<String> permisos;

    RolUsuario(List<String> permisos) {
        this.permisos = permisos;
    }

    public List<String> getPermisos() {
        return Collections.unmodifiableList(permisos);
    }

    public boolean tienePermiso(String permiso) {
        return permisos.contains(permiso);
    }
}
