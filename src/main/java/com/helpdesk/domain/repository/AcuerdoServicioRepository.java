package com.helpdesk.domain.repository;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.enums.NivelPrioridad;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para acuerdos de nivel de servicio (SLA).
 */
public interface AcuerdoServicioRepository {

    /**
     * Persiste o actualiza un acuerdo de servicio.
     */
    AcuerdoServicio save(AcuerdoServicio acuerdo);

    /**
     * Busca un acuerdo por identificador.
     */
    Optional<AcuerdoServicio> findById(Long id);

    /**
     * Lista todos los acuerdos activos.
     */
    List<AcuerdoServicio> findAllActivos();

    /**
     * Busca el acuerdo activo asociado a un nivel de prioridad.
     */
    Optional<AcuerdoServicio> findActivoByNivelPrioridad(NivelPrioridad prioridad);

    /**
     * Elimina un acuerdo por identificador.
     */
    void deleteById(Long id);
}
