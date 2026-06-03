package com.helpdesk.domain.service;

import com.helpdesk.domain.entity.Asignacion;
import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.exception.DomainException;
import com.helpdesk.domain.repository.AsignacionRepository;
import com.helpdesk.domain.repository.UsuarioRepository;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/**
 * Servicio de dominio para reglas de asignación de incidencias a técnicos.
 */
public class AsignacionService {

    private static final EnumSet<NivelPrioridad> PRIORIDADES_ASIGNACION_MANUAL =
            EnumSet.of(NivelPrioridad.CRITICA, NivelPrioridad.ALTA);

    private final UsuarioRepository usuarioRepository;
    private final AsignacionRepository asignacionRepository;

    public AsignacionService(UsuarioRepository usuarioRepository, AsignacionRepository asignacionRepository) {
        this.usuarioRepository = Objects.requireNonNull(usuarioRepository, "usuarioRepository");
        this.asignacionRepository = Objects.requireNonNull(asignacionRepository, "asignacionRepository");
    }

    /**
     * Determina el técnico con menor carga activa para asignación automática.
     * En empate se elige el de menor identificador.
     *
     * @param prioridad       prioridad de la incidencia (reservado para reglas futuras)
     * @param tecnicosActivos lista de técnicos candidatos
     * @return identificador del técnico seleccionado
     */
    public Long determinarTecnicoAutomatico(NivelPrioridad prioridad, List<Usuario> tecnicosActivos) {
        Objects.requireNonNull(prioridad, "prioridad");
        List<Usuario> candidatos = tecnicosActivos.stream()
                .filter(u -> u.getRol() == RolUsuario.TECNICO && u.isActivo())
                .toList();

        if (candidatos.isEmpty()) {
            throw new DomainException("No hay técnicos activos disponibles para asignación automática");
        }

        return candidatos.stream()
                .min(Comparator
                        .comparingLong(this::contarAsignacionesActivas)
                        .thenComparing(Usuario::getId, Comparator.nullsLast(Long::compareTo)))
                .map(Usuario::getId)
                .orElseThrow(() -> new DomainException("No se pudo determinar técnico automático"));
    }

    /**
     * Indica si la prioridad exige intervención manual del administrador.
     */
    public boolean requiereAsignacionManual(NivelPrioridad prioridad) {
        return prioridad != null && PRIORIDADES_ASIGNACION_MANUAL.contains(prioridad);
    }

    /**
     * Valida que el técnico exista, esté activo y tenga rol de soporte.
     *
     * @param tecnicoId identificador del técnico
     */
    public void validarDisponibilidadTecnico(Long tecnicoId) {
        if (tecnicoId == null) {
            throw new DomainException("El identificador del técnico es obligatorio");
        }

        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new DomainException("Técnico no encontrado: " + tecnicoId));

        if (!tecnico.isActivo()) {
            throw new DomainException("El técnico no está activo: " + tecnicoId);
        }
        if (tecnico.getRol() != RolUsuario.TECNICO) {
            throw new DomainException("El usuario no tiene rol de técnico: " + tecnicoId);
        }
    }

    private long contarAsignacionesActivas(Usuario tecnico) {
        if (tecnico.getId() == null) {
            return Long.MAX_VALUE;
        }
        return asignacionRepository.findByTecnicoId(tecnico.getId()).stream()
                .filter(Asignacion::isActiva)
                .count();
    }
}
