package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.infrastructure.persistence.entity.IncidenciaJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataIncidenciaRepository extends JpaRepository<IncidenciaJpaEntity, Long> {

    Page<IncidenciaJpaEntity> findByEstado(EstadoIncidencia estado, Pageable pageable);

    Page<IncidenciaJpaEntity> findByClienteId(Long clienteId, Pageable pageable);

    Page<IncidenciaJpaEntity> findByTecnicoAsignadoId(Long tecnicoId, Pageable pageable);

    List<IncidenciaJpaEntity> findByEstadoAndFechaCreacionBefore(EstadoIncidencia estado, LocalDateTime fecha);

    long countByEstado(EstadoIncidencia estado);

    @Query("""
            select i from IncidenciaJpaEntity i
            where i.slaViolado = false
              and i.estado not in (com.helpdesk.domain.enums.EstadoIncidencia.RESUELTO, com.helpdesk.domain.enums.EstadoIncidencia.CERRADO)
              and i.fechaCreacion < :ahora
            """)
    List<IncidenciaJpaEntity> findVencidas(@Param("ahora") LocalDateTime ahora);
}
