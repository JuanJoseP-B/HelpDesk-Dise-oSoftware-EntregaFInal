package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Comentario;
import com.helpdesk.domain.repository.ComentarioRepository;
import com.helpdesk.infrastructure.persistence.mapper.ComentarioPersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class JpaComentarioRepositoryAdapter implements ComentarioRepository {

    private final SpringDataComentarioRepository repository;
    private final ComentarioPersistenceMapper mapper;

    public JpaComentarioRepositoryAdapter(SpringDataComentarioRepository repository, ComentarioPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Comentario save(Comentario comentario) {
        return mapper.toDomain(repository.save(mapper.toEntity(comentario)));
    }

    @Override
    public Optional<Comentario> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Comentario> findByIncidenciaId(Long incidenciaId) {
        return repository.findByIncidenciaId(incidenciaId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Comentario> findByIncidenciaIdAndVisibleParaClienteTrue(Long incidenciaId) {
        return repository.findByIncidenciaIdAndVisibleParaClienteTrue(incidenciaId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
