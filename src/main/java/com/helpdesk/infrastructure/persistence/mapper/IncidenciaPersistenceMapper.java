package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.infrastructure.persistence.entity.IncidenciaJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncidenciaPersistenceMapper {

    default Incidencia toDomain(IncidenciaJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Incidencia.reconstituir(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getFechaCreacion(),
                entity.getFechaAsignacion(),
                entity.getFechaResolucion(),
                entity.getFechaCierre(),
                entity.getEstado(),
                entity.getNivelPrioridad(),
                entity.getSolucion(),
                entity.getTecnicoAsignadoId(),
                entity.getClienteId(),
                entity.getAcuerdoServicioId(),
                entity.isSlaViolado()
        );
    }

    IncidenciaJpaEntity toEntity(Incidencia domain);
}
