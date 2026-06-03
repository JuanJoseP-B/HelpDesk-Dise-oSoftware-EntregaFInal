package com.helpdesk.infrastructure.persistence.mapper;

import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioPersistenceMapper {

    default Usuario toDomain(UsuarioJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                entity.getCorreoElectronico(),
                entity.getTelefono(),
                entity.getContrasenaHash(),
                entity.isActivo(),
                entity.getRol(),
                entity.getFechaRegistro(),
                entity.getUltimoAcceso()
        );
    }

    default UsuarioJpaEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }
        UsuarioJpaEntity entity = new UsuarioJpaEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setCorreoElectronico(domain.getCorreoElectronico());
        entity.setTelefono(domain.getTelefono());
        entity.setContrasenaHash(domain.getContrasenaHash());
        entity.setActivo(domain.isActivo());
        entity.setRol(domain.getRol());
        entity.setFechaRegistro(domain.getFechaRegistro());
        entity.setUltimoAcceso(domain.getUltimoAcceso());
        return entity;
    }
}
