package com.helpdesk.domain.repository;

import com.helpdesk.domain.entity.HistorialEstado;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para el historial de cambios de estado.
 */
public interface HistorialEstadoRepository {

    /**
     * Persiste un registro de historial.
     */
    HistorialEstado save(HistorialEstado historial);

    /**
     * Lista el historial de una incidencia ordenado por fecha descendente.
     */
    List<HistorialEstado> findByIncidenciaIdOrderByFechaCambioDesc(Long incidenciaId);

    /**
     * Obtiene el último cambio de estado registrado para una incidencia.
     */
    Optional<HistorialEstado> findUltimoByIncidenciaId(Long incidenciaId);
}
