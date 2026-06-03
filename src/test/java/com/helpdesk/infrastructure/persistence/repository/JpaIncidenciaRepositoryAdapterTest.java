package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.pagination.Paginacion;
import com.helpdesk.infrastructure.persistence.mapper.IncidenciaPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaIncidenciaRepositoryAdapter.class, IncidenciaPersistenceMapperImpl.class})
@TestPropertySource(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
class JpaIncidenciaRepositoryAdapterTest {

    @Autowired
    private JpaIncidenciaRepositoryAdapter adapter;

    @Test
    void guardaYConsultaPorEstado() {
        Incidencia incidencia = new Incidencia(
                null,
                "Equipo sin red",
                "No hay conectividad",
                LocalDateTime.now(),
                NivelPrioridad.ALTA,
                10L,
                1L
        );

        Incidencia guardada = adapter.save(incidencia);

        assertThat(adapter.findById(guardada.getId())).isPresent();
        assertThat(adapter.findByEstado(EstadoIncidencia.ABIERTO, new Paginacion(0, 10)).contenido()).hasSize(1);
        assertThat(adapter.countByEstado(EstadoIncidencia.ABIERTO)).isEqualTo(1);
    }
}
