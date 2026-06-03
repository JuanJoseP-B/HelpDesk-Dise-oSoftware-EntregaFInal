package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.Notificacion;
import com.helpdesk.infrastructure.persistence.entity.NotificacionJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificacionPersistenceMapper {

    default Notificacion toDomain(NotificacionJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Notificacion(
                entity.getId(),
                entity.getDestinatarioId(),
                entity.getTipo(),
                entity.getAsunto(),
                entity.getMensaje(),
                entity.getFechaEnvio(),
                entity.getEstadoEnvio(),
                entity.isLeida()
        );
    }

    default NotificacionJpaEntity toEntity(Notificacion domain) {
        if (domain == null) {
            return null;
        }
        NotificacionJpaEntity entity = new NotificacionJpaEntity();
        entity.setId(domain.getId());
        entity.setDestinatarioId(domain.getDestinatarioId());
        entity.setTipo(domain.getTipo());
        entity.setAsunto(domain.getAsunto());
        entity.setMensaje(domain.getMensaje());
        entity.setFechaEnvio(domain.getFechaEnvio());
        entity.setEstadoEnvio(domain.getEstadoEnvio());
        entity.setLeida(domain.isLeida());
        return entity;
    }
}
