package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.infrastructure.persistence.entity.AcuerdoServicioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataAcuerdoServicioRepository extends JpaRepository<AcuerdoServicioJpaEntity, Long> {

    List<AcuerdoServicioJpaEntity> findByActivoTrue();

    Optional<AcuerdoServicioJpaEntity> findFirstByNivelPrioridadAndActivoTrue(NivelPrioridad nivelPrioridad);
}
