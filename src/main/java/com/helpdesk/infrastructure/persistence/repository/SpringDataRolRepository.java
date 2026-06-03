package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.infrastructure.persistence.entity.RolJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataRolRepository extends JpaRepository<RolJpaEntity, Long> {

    Optional<RolJpaEntity> findByTipo(RolUsuario tipo);
}
