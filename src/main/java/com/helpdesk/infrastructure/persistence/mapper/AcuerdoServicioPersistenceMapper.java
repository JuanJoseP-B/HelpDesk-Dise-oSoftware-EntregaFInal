package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.infrastructure.persistence.entity.AcuerdoServicioJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AcuerdoServicioPersistenceMapper {

    default AcuerdoServicio toDomain(AcuerdoServicioJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AcuerdoServicio(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getNivelPrioridad(),
                entity.getTiempoMaxRespuestaHoras(),
                entity.getTiempoMaxResolucionHoras(),
                entity.isActivo(),
                entity.getFechaCreacion()
        );
    }

    default AcuerdoServicioJpaEntity toEntity(AcuerdoServicio domain) {
        if (domain == null) {
            return null;
        }
        AcuerdoServicioJpaEntity entity = new AcuerdoServicioJpaEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setDescripcion(domain.getDescripcion());
        entity.setNivelPrioridad(domain.getNivelPrioridad());
        entity.setTiempoMaxRespuestaHoras(domain.getTiempoMaxRespuestaHoras());
        entity.setTiempoMaxResolucionHoras(domain.getTiempoMaxResolucionHoras());
        entity.setActivo(domain.isActivo());
        entity.setFechaCreacion(domain.getFechaCreacion());
        return entity;
    }
}
