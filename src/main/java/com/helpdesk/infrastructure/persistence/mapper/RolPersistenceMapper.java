package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.Rol;
import com.helpdesk.infrastructure.persistence.entity.RolJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolPersistenceMapper {

    Rol toDomain(RolJpaEntity entity);

    RolJpaEntity toEntity(Rol domain);
}
