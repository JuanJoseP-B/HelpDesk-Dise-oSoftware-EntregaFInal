package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.Comentario;
import com.helpdesk.infrastructure.persistence.entity.ComentarioJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComentarioPersistenceMapper {

    default Comentario toDomain(ComentarioJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Comentario(
                entity.getId(),
                entity.getIncidenciaId(),
                entity.getAutorId(),
                entity.getContenido(),
                entity.getFechaHora(),
                entity.isVisibleParaCliente()
        );
    }

    default ComentarioJpaEntity toEntity(Comentario domain) {
        if (domain == null) {
            return null;
        }
        ComentarioJpaEntity entity = new ComentarioJpaEntity();
        entity.setId(domain.getId());
        entity.setIncidenciaId(domain.getIncidenciaId());
        entity.setAutorId(domain.getAutorId());
        entity.setContenido(domain.getContenido());
        entity.setFechaHora(domain.getFechaHora());
        entity.setVisibleParaCliente(domain.isVisibleParaCliente());
        return entity;
    }
}
