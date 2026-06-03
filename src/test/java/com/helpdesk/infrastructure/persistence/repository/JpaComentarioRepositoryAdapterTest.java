package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Comentario;
import com.helpdesk.infrastructure.persistence.mapper.ComentarioPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaComentarioRepositoryAdapter.class, ComentarioPersistenceMapperImpl.class})
@TestPropertySource(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
class JpaComentarioRepositoryAdapterTest {

    @Autowired
    private JpaComentarioRepositoryAdapter adapter;

    @Test
    void filtraComentariosVisiblesParaCliente() {
        adapter.save(new Comentario(null, 1L, 2L, "Visible", LocalDateTime.now(), true));
        adapter.save(new Comentario(null, 1L, 3L, "Interno", LocalDateTime.now(), false));

        assertThat(adapter.findByIncidenciaId(1L)).hasSize(2);
        assertThat(adapter.findByIncidenciaIdAndVisibleParaClienteTrue(1L)).singleElement()
                .matches(Comentario::isVisibleParaCliente);
    }
}
