package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.Asignacion;
import com.helpdesk.infrastructure.persistence.entity.AsignacionJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AsignacionPersistenceMapper {

    default Asignacion toDomain(AsignacionJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Asignacion(
                entity.getId(),
                entity.getIncidenciaId(),
                entity.getTecnicoId(),
                entity.getAsignadoPorId(),
                entity.getFechaAsignacion(),
                entity.getMotivo(),
                entity.isActiva(),
                entity.isAutomatica()
        );
    }

    default AsignacionJpaEntity toEntity(Asignacion domain) {
        if (domain == null) {
            return null;
        }
        AsignacionJpaEntity entity = new AsignacionJpaEntity();
        entity.setId(domain.getId());
        entity.setIncidenciaId(domain.getIncidenciaId());
        entity.setTecnicoId(domain.getTecnicoId());
        entity.setAsignadoPorId(domain.getAsignadoPorId());
        entity.setFechaAsignacion(domain.getFechaAsignacion());
        entity.setMotivo(domain.getMotivo());
        entity.setActiva(domain.isActiva());
        entity.setAutomatica(domain.esAutomatica());
        return entity;
    }
}
