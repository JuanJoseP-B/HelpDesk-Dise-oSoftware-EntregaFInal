package com.helpdesk.domain.repository;

import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.pagination.Pagina;
import com.helpdesk.domain.pagination.Paginacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para el agregado {@link Incidencia}.
 */
public interface IncidenciaRepository {

    /**
     * Persiste o actualiza una incidencia.
     */
    Incidencia save(Incidencia incidencia);

    /**
     * Busca una incidencia por identificador.
     */
    Optional<Incidencia> findById(Long id);

    /**
     * Obtiene todas las incidencias sin paginación.
     */
    List<Incidencia> findAll();

    /**
     * Lista incidencias filtradas por estado de forma paginada.
     */
    Pagina<Incidencia> findByEstado(EstadoIncidencia estado, Paginacion paginacion);

    /**
     * Lista incidencias de un cliente de forma paginada.
     */
    Pagina<Incidencia> findByClienteId(Long clienteId, Paginacion paginacion);

    /**
     * Lista incidencias asignadas a un técnico de forma paginada.
     */
    Pagina<Incidencia> findByTecnicoAsignadoId(Long tecnicoId, Paginacion paginacion);

    /**
     * Busca incidencias en un estado creadas antes de la fecha indicada.
     */
    List<Incidencia> findByEstadoAndFechaCreacionBefore(EstadoIncidencia estado, LocalDateTime fecha);

    /**
     * Obtiene incidencias con SLA vencido respecto al instante dado.
     */
    List<Incidencia> findVencidas(LocalDateTime ahora);

    /**
     * Indica si existe una incidencia con el identificador dado.
     */
    boolean existsById(Long id);

    /**
     * Cuenta incidencias en un estado concreto.
     */
    long countByEstado(EstadoIncidencia estado);

    /**
     * Elimina una incidencia por identificador.
     */
    void deleteById(Long id);
}
