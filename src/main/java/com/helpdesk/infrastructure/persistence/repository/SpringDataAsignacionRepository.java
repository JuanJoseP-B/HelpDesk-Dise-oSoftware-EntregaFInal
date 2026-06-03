package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.infrastructure.persistence.entity.AsignacionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataAsignacionRepository extends JpaRepository<AsignacionJpaEntity, Long> {

    List<AsignacionJpaEntity> findByIncidenciaId(Long incidenciaId);

    Optional<AsignacionJpaEntity> findFirstByIncidenciaIdAndActivaTrue(Long incidenciaId);

    List<AsignacionJpaEntity> findByTecnicoId(Long tecnicoId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update AsignacionJpaEntity a set a.activa = false where a.incidenciaId = :incidenciaId and a.activa = true")
    void desactivarAsignacionesAnteriores(@Param("incidenciaId") Long incidenciaId);
}
