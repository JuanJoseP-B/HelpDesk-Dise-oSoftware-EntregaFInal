package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.pagination.Paginacion;
import com.helpdesk.infrastructure.persistence.mapper.UsuarioPersistenceMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaUsuarioRepositoryAdapter.class, UsuarioPersistenceMapperImpl.class})
@TestPropertySource(properties = {"spring.sql.init.mode=never", "spring.jpa.hibernate.ddl-auto=create-drop"})
class JpaUsuarioRepositoryAdapterTest {

    @Autowired
    private JpaUsuarioRepositoryAdapter adapter;

    @Test
    void guardaYConsultaPorCorreo() {
        Usuario usuario = new Usuario(
                null,
                "Cliente Uno",
                "cliente.integration@test.com",
                "3000000000",
                "hash",
                true,
                RolUsuario.CLIENTE,
                LocalDateTime.now(),
                null
        );

        Usuario guardado = adapter.save(usuario);

        assertThat(adapter.findById(guardado.getId())).isPresent();
        assertThat(adapter.findByCorreoElectronico("cliente.integration@test.com")).isPresent();
        assertThat(adapter.findAll(new Paginacion(0, 10)).contenido()).hasSize(1);
    }
}
