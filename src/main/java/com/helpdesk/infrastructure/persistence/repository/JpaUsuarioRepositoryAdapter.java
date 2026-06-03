package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.pagination.Pagina;
import com.helpdesk.domain.pagination.Paginacion;
import com.helpdesk.domain.repository.UsuarioRepository;
import com.helpdesk.infrastructure.persistence.mapper.PaginaMapper;
import com.helpdesk.infrastructure.persistence.mapper.UsuarioPersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class JpaUsuarioRepositoryAdapter implements UsuarioRepository {

    private final SpringDataUsuarioRepository repository;
    private final UsuarioPersistenceMapper mapper;

    public JpaUsuarioRepositoryAdapter(SpringDataUsuarioRepository repository, UsuarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return mapper.toDomain(repository.save(mapper.toEntity(usuario)));
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByCorreoElectronico(String email) {
        return repository.findByCorreoElectronico(email).map(mapper::toDomain);
    }

    @Override
    public List<Usuario> findByRol(RolUsuario rol) {
        return repository.findByRol(rol).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Usuario> findActivos() {
        return repository.findByActivoTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Pagina<Usuario> findAll(Paginacion paginacion) {
        return PaginaMapper.toPagina(repository.findAll(PaginaMapper.toPageable(paginacion)), mapper::toDomain);
    }

    @Override
    public boolean existsByCorreoElectronico(String email) {
        return repository.existsByCorreoElectronico(email);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
