package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.infrastructure.persistence.mapper.AcuerdoServicioPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAcuerdoServicioRepositoryAdapter.class, AcuerdoServicioPersistenceMapperImpl.class})
@TestPropertySource(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
class JpaAcuerdoServicioRepositoryAdapterTest {

    @Autowired
    private JpaAcuerdoServicioRepositoryAdapter adapter;

    @Test
    void consultaActivoPorPrioridad() {
        adapter.save(new AcuerdoServicio(null, "SLA Alta", "Alta", NivelPrioridad.ALTA, 2, 8, true, LocalDateTime.now()));

        assertThat(adapter.findActivoByNivelPrioridad(NivelPrioridad.ALTA)).isPresent();
        assertThat(adapter.findAllActivos()).hasSize(1);
    }
}
