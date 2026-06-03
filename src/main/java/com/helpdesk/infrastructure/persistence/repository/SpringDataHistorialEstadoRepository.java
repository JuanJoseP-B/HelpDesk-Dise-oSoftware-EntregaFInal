package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.infrastructure.persistence.entity.HistorialEstadoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataHistorialEstadoRepository extends JpaRepository<HistorialEstadoJpaEntity, Long> {

    List<HistorialEstadoJpaEntity> findByIncidenciaIdOrderByFechaCambioDesc(Long incidenciaId);

    Optional<HistorialEstadoJpaEntity> findFirstByIncidenciaIdOrderByFechaCambioDesc(Long incidenciaId);
}
