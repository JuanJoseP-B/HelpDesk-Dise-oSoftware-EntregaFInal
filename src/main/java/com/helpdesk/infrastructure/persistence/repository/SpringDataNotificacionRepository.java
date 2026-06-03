package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.infrastructure.persistence.entity.NotificacionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataNotificacionRepository extends JpaRepository<NotificacionJpaEntity, Long> {

    List<NotificacionJpaEntity> findByEstadoEnvio(String estadoEnvio);

    List<NotificacionJpaEntity> findByDestinatarioId(Long destinatarioId);
}
