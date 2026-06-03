package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.HistorialEstado;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.infrastructure.persistence.mapper.HistorialEstadoPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaHistorialEstadoRepositoryAdapter.class, HistorialEstadoPersistenceMapperImpl.class})
@TestPropertySource(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
class JpaHistorialEstadoRepositoryAdapterTest {

    @Autowired
    private JpaHistorialEstadoRepositoryAdapter adapter;

    @Test
    void obtieneUltimoCambioPorIncidencia() {
        adapter.save(new HistorialEstado(null, 1L, EstadoIncidencia.ABIERTO, EstadoIncidencia.ASIGNADO, LocalDateTime.now().minusMinutes(5), 9L, "Asignacion"));
        adapter.save(new HistorialEstado(null, 1L, EstadoIncidencia.ASIGNADO, EstadoIncidencia.EN_PROGRESO, LocalDateTime.now(), 2L, "Inicio"));

        assertThat(adapter.findByIncidenciaIdOrderByFechaCambioDesc(1L)).hasSize(2);
        assertThat(adapter.findUltimoByIncidenciaId(1L)).get()
                .extracting(HistorialEstado::getEstadoNuevo)
                .isEqualTo(EstadoIncidencia.EN_PROGRESO);
    }
}
