package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.HistorialEstado;
import com.helpdesk.domain.repository.HistorialEstadoRepository;
import com.helpdesk.infrastructure.persistence.mapper.HistorialEstadoPersistenceMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class JpaHistorialEstadoRepositoryAdapter implements HistorialEstadoRepository {

    private final SpringDataHistorialEstadoRepository repository;
    private final HistorialEstadoPersistenceMapper mapper;

    public JpaHistorialEstadoRepositoryAdapter(
            SpringDataHistorialEstadoRepository repository,
            HistorialEstadoPersistenceMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public HistorialEstado save(HistorialEstado historial) {
        return mapper.toDomain(repository.save(mapper.toEntity(historial)));
    }

    @Override
    public List<HistorialEstado> findByIncidenciaIdOrderByFechaCambioDesc(Long incidenciaId) {
        return repository.findByIncidenciaIdOrderByFechaCambioDesc(incidenciaId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<HistorialEstado> findUltimoByIncidenciaId(Long incidenciaId) {
        return repository.findFirstByIncidenciaIdOrderByFechaCambioDesc(incidenciaId).map(mapper::toDomain);
    }
}
