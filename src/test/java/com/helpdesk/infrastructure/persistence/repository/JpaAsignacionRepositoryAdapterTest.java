package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Asignacion;
import com.helpdesk.infrastructure.persistence.mapper.AsignacionPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAsignacionRepositoryAdapter.class, AsignacionPersistenceMapperImpl.class})
@TestPropertySource(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
class JpaAsignacionRepositoryAdapterTest {

    @Autowired
    private JpaAsignacionRepositoryAdapter adapter;

    @Test
    void desactivaAsignacionesPrevias() {
        adapter.save(new Asignacion(null, 1L, 2L, 9L, LocalDateTime.now(), "Inicial", true, false));

        adapter.desactivarAsignacionesAnteriores(1L);

        assertThat(adapter.findActivaByIncidenciaId(1L)).isEmpty();
        assertThat(adapter.findByIncidenciaId(1L)).singleElement().matches(asignacion -> !asignacion.isActiva());
    }
}
