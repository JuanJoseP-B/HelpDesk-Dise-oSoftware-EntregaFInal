package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Notificacion;
import com.helpdesk.domain.repository.NotificacionRepository;
import com.helpdesk.infrastructure.persistence.mapper.NotificacionPersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional(readOnly = true)
public class JpaNotificacionRepositoryAdapter implements NotificacionRepository {

    private final SpringDataNotificacionRepository repository;
    private final NotificacionPersistenceMapper mapper;

    public JpaNotificacionRepositoryAdapter(
            SpringDataNotificacionRepository repository,
            NotificacionPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Notificacion save(Notificacion notificacion) {
        return mapper.toDomain(repository.save(mapper.toEntity(notificacion)));
    }

    @Override
    public List<Notificacion> findPendientes() {
        return repository.findByEstadoEnvio(Notificacion.ESTADO_PENDIENTE).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Notificacion> findByDestinatarioId(Long destinatarioId) {
        return repository.findByDestinatarioId(destinatarioId).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void marcarEnviado(Long notificacionId) {
        repository.findById(notificacionId).ifPresent(entity -> {
            entity.setEstadoEnvio(Notificacion.ESTADO_ENVIADO);
            entity.setFechaEnvio(java.time.LocalDateTime.now());
            repository.save(entity);
        });
    }

    @Override
    @Transactional
    public void marcarFallido(Long notificacionId, String error) {
        repository.findById(notificacionId).ifPresent(entity -> {
            entity.setEstadoEnvio(Notificacion.ESTADO_FALLIDO);
            entity.setMensaje(entity.getMensaje() + " [Error: " + error + "]");
            repository.save(entity);
        });
    }
}
