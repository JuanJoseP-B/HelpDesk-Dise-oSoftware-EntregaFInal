package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.pagination.Pagina;
import com.helpdesk.domain.pagination.Paginacion;
import com.helpdesk.domain.repository.IncidenciaRepository;
import com.helpdesk.infrastructure.persistence.mapper.IncidenciaPersistenceMapper;
import com.helpdesk.infrastructure.persistence.mapper.PaginaMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class JpaIncidenciaRepositoryAdapter implements IncidenciaRepository {

    private final SpringDataIncidenciaRepository repository;
    private final IncidenciaPersistenceMapper mapper;

    public JpaIncidenciaRepositoryAdapter(SpringDataIncidenciaRepository repository, IncidenciaPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Incidencia save(Incidencia incidencia) {
        return mapper.toDomain(repository.save(mapper.toEntity(incidencia)));
    }

    @Override
    public Optional<Incidencia> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Incidencia> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Pagina<Incidencia> findByEstado(EstadoIncidencia estado, Paginacion paginacion) {
        return PaginaMapper.toPagina(repository.findByEstado(estado, PaginaMapper.toPageable(paginacion)), mapper::toDomain);
    }

    @Override
    public Pagina<Incidencia> findByClienteId(Long clienteId, Paginacion paginacion) {
        return PaginaMapper.toPagina(repository.findByClienteId(clienteId, PaginaMapper.toPageable(paginacion)), mapper::toDomain);
    }

    @Override
    public Pagina<Incidencia> findByTecnicoAsignadoId(Long tecnicoId, Paginacion paginacion) {
        return PaginaMapper.toPagina(repository.findByTecnicoAsignadoId(tecnicoId, PaginaMapper.toPageable(paginacion)), mapper::toDomain);
    }

    @Override
    public List<Incidencia> findByEstadoAndFechaCreacionBefore(EstadoIncidencia estado, LocalDateTime fecha) {
        return repository.findByEstadoAndFechaCreacionBefore(estado, fecha).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Incidencia> findVencidas(LocalDateTime ahora) {
        return repository.findVencidas(ahora).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public long countByEstado(EstadoIncidencia estado) {
        return repository.countByEstado(estado);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
