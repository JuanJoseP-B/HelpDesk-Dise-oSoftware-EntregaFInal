package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.repository.AcuerdoServicioRepository;
import com.helpdesk.infrastructure.persistence.mapper.AcuerdoServicioPersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class JpaAcuerdoServicioRepositoryAdapter implements AcuerdoServicioRepository {

    private final SpringDataAcuerdoServicioRepository repository;
    private final AcuerdoServicioPersistenceMapper mapper;

    public JpaAcuerdoServicioRepositoryAdapter(
            SpringDataAcuerdoServicioRepository repository,
            AcuerdoServicioPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public AcuerdoServicio save(AcuerdoServicio acuerdo) {
        return mapper.toDomain(repository.save(mapper.toEntity(acuerdo)));
    }

    @Override
    public Optional<AcuerdoServicio> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AcuerdoServicio> findAllActivos() {
        return repository.findByActivoTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<AcuerdoServicio> findActivoByNivelPrioridad(NivelPrioridad prioridad) {
        return repository.findFirstByNivelPrioridadAndActivoTrue(prioridad).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
