package com.helpdesk.domain.repository;

import com.helpdesk.domain.entity.Asignacion;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para {@link Asignacion}.
 */
public interface AsignacionRepository {

    /**
     * Persiste o actualiza una asignación.
     */
    Asignacion save(Asignacion asignacion);

    /**
     * Busca una asignación por identificador.
     */
    Optional<Asignacion> findById(Long id);

    /**
     * Lista el historial de asignaciones de una incidencia.
     */
    List<Asignacion> findByIncidenciaId(Long incidenciaId);

    /**
     * Obtiene la asignación activa de una incidencia, si existe.
     */
    Optional<Asignacion> findActivaByIncidenciaId(Long incidenciaId);

    /**
     * Lista asignaciones realizadas a un técnico.
     */
    List<Asignacion> findByTecnicoId(Long tecnicoId);

    /**
     * Desactiva todas las asignaciones previas de una incidencia.
     */
    void desactivarAsignacionesAnteriores(Long incidenciaId);
}
