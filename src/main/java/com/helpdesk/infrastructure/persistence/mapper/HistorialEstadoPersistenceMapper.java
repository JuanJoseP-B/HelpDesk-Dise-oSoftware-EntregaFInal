package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.HistorialEstado;
import com.helpdesk.infrastructure.persistence.entity.HistorialEstadoJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HistorialEstadoPersistenceMapper {

    default HistorialEstado toDomain(HistorialEstadoJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new HistorialEstado(
                entity.getId(),
                entity.getIncidenciaId(),
                entity.getEstadoAnterior(),
                entity.getEstadoNuevo(),
                entity.getFechaCambio(),
                entity.getUsuarioCambioId(),
                entity.getMotivo()
        );
    }

    default HistorialEstadoJpaEntity toEntity(HistorialEstado domain) {
        if (domain == null) {
            return null;
        }
        HistorialEstadoJpaEntity entity = new HistorialEstadoJpaEntity();
        entity.setId(domain.getId());
        entity.setIncidenciaId(domain.getIncidenciaId());
        entity.setEstadoAnterior(domain.getEstadoAnterior());
        entity.setEstadoNuevo(domain.getEstadoNuevo());
        entity.setFechaCambio(domain.getFechaCambio());
        entity.setUsuarioCambioId(domain.getUsuarioCambioId());
        entity.setMotivo(domain.getMotivo());
        return entity;
    }
}
