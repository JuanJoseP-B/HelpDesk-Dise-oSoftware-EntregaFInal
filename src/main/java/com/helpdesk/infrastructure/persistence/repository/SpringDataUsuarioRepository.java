package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long> {

    Optional<UsuarioJpaEntity> findByCorreoElectronico(String correoElectronico);

    List<UsuarioJpaEntity> findByRol(RolUsuario rol);

    List<UsuarioJpaEntity> findByActivoTrue();

    boolean existsByCorreoElectronico(String correoElectronico);
}
