package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Asignacion;
import com.helpdesk.domain.repository.AsignacionRepository;
import com.helpdesk.infrastructure.persistence.mapper.AsignacionPersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class JpaAsignacionRepositoryAdapter implements AsignacionRepository {

    private final SpringDataAsignacionRepository repository;
    private final AsignacionPersistenceMapper mapper;

    public JpaAsignacionRepositoryAdapter(SpringDataAsignacionRepository repository, AsignacionPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Asignacion save(Asignacion asignacion) {
        return mapper.toDomain(repository.save(mapper.toEntity(asignacion)));
    }

    @Override
    public Optional<Asignacion> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Asignacion> findByIncidenciaId(Long incidenciaId) {
        return repository.findByIncidenciaId(incidenciaId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Asignacion> findActivaByIncidenciaId(Long incidenciaId) {
        return repository.findFirstByIncidenciaIdAndActivaTrue(incidenciaId).map(mapper::toDomain);
    }

    @Override
    public List<Asignacion> findByTecnicoId(Long tecnicoId) {
        return repository.findByTecnicoId(tecnicoId).stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void desactivarAsignacionesAnteriores(Long incidenciaId) {
        repository.desactivarAsignacionesAnteriores(incidenciaId);
    }
}
