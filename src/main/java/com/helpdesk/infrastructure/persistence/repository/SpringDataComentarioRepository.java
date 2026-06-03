package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.infrastructure.persistence.entity.ComentarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataComentarioRepository extends JpaRepository<ComentarioJpaEntity, Long> {

    List<ComentarioJpaEntity> findByIncidenciaId(Long incidenciaId);

    List<ComentarioJpaEntity> findByIncidenciaIdAndVisibleParaClienteTrue(Long incidenciaId);
}
