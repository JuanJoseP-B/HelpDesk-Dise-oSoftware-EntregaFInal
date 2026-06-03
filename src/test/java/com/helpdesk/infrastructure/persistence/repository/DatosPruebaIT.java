package com.helpdesk.infrastructure.persistence.repository;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.RolUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "/data-test.sql")
class DatosPruebaIT {

    @Autowired
    private SpringDataUsuarioRepository usuarioRepository;

    @Autowired
    private SpringDataIncidenciaRepository incidenciaRepository;

    @Test
    void cargaDatosBaseParaPruebasDeIntegracion() {
        assertThat(usuarioRepository.existsByCorreoElectronico("admin@test.com")).isTrue();
        assertThat(usuarioRepository.findByRol(RolUsuario.TECNICO)).hasSize(1);
        assertThat(incidenciaRepository.findByEstado(EstadoIncidencia.ABIERTO, PageRequest.of(0, 10))).hasSize(1);
    }
}
