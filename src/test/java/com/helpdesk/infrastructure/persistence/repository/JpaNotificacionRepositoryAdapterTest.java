package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Notificacion;
import com.helpdesk.domain.enums.TipoNotificacion;
import com.helpdesk.infrastructure.persistence.mapper.NotificacionPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaNotificacionRepositoryAdapter.class, NotificacionPersistenceMapperImpl.class})
@TestPropertySource(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
class JpaNotificacionRepositoryAdapterTest {

    @Autowired
    private JpaNotificacionRepositoryAdapter adapter;

    @Test
    void marcaNotificacionComoEnviada() {
        Notificacion guardada = adapter.save(new Notificacion(
                null,
                4L,
                TipoNotificacion.CREACION_TICKET,
                "Ticket creado",
                "Mensaje",
                null,
                Notificacion.ESTADO_PENDIENTE,
                false
        ));

        adapter.marcarEnviado(guardada.getId());

        assertThat(adapter.findPendientes()).isEmpty();
        assertThat(adapter.findByDestinatarioId(4L)).singleElement()
                .extracting(Notificacion::getEstadoEnvio)
                .isEqualTo(Notificacion.ESTADO_ENVIADO);
    }
}
