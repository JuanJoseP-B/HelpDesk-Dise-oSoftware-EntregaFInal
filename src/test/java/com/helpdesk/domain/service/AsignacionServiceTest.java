package com.helpdesk.domain.service;

import com.helpdesk.domain.entity.Asignacion;
import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.exception.DomainException;
import com.helpdesk.domain.pagination.Pagina;
import com.helpdesk.domain.pagination.Paginacion;
import com.helpdesk.domain.repository.AsignacionRepository;
import com.helpdesk.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsignacionServiceTest {

    private AsignacionService asignacionService;
    private UsuarioRepositoryFake usuarioRepository;
    private AsignacionRepositoryFake asignacionRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = new UsuarioRepositoryFake();
        asignacionRepository = new AsignacionRepositoryFake();
        asignacionService = new AsignacionService(usuarioRepository, asignacionRepository);
    }

    @Test
    @DisplayName("CRÍTICA y ALTA requieren asignación manual")
    void prioridadesRequierenAsignacionManual() {
        assertTrue(asignacionService.requiereAsignacionManual(NivelPrioridad.CRITICA));
        assertTrue(asignacionService.requiereAsignacionManual(NivelPrioridad.ALTA));
        assertFalse(asignacionService.requiereAsignacionManual(NivelPrioridad.MEDIA));
        assertFalse(asignacionService.requiereAsignacionManual(NivelPrioridad.BAJA));
    }

    @Test
    @DisplayName("Selecciona técnico con menos asignaciones activas")
    void seleccionaTecnicoConMenorCarga() {
        Usuario t1 = tecnico(1L);
        Usuario t2 = tecnico(2L);
        asignacionRepository.guardar(asignacionActiva(1L, 1L));
        asignacionRepository.guardar(asignacionActiva(2L, 1L));
        asignacionRepository.guardar(asignacionActiva(3L, 1L));

        Long seleccionado = asignacionService.determinarTecnicoAutomatico(
                NivelPrioridad.MEDIA,
                List.of(t1, t2)
        );
        assertEquals(2L, seleccionado);
    }

    @Test
    @DisplayName("Valida técnico activo existente")
    void validaTecnicoDisponible() {
        usuarioRepository.guardar(tecnico(5L));
        asignacionService.validarDisponibilidadTecnico(5L);
    }

    @Test
    @DisplayName("Rechaza técnico inexistente o inactivo")
    void rechazaTecnicoNoDisponible() {
        usuarioRepository.guardar(tecnico(1L));
        Usuario inactivo = tecnico(2L);
        inactivo.desactivar();
        usuarioRepository.guardar(inactivo);

        assertThrows(DomainException.class, () -> asignacionService.validarDisponibilidadTecnico(99L));
        assertThrows(DomainException.class, () -> asignacionService.validarDisponibilidadTecnico(2L));
    }

    private static Usuario tecnico(Long id) {
        return new Usuario(
                id,
                "Técnico " + id,
                "tec" + id + "@helpdesk.com",
                "600000000",
                "hash",
                true,
                RolUsuario.TECNICO,
                LocalDateTime.now(),
                null
        );
    }

    private static Asignacion asignacionActiva(Long incidenciaId, Long tecnicoId) {
        return new Asignacion(
                null,
                incidenciaId,
                tecnicoId,
                1L,
                LocalDateTime.now(),
                "auto",
                true,
                true
        );
    }

    private static final class UsuarioRepositoryFake implements UsuarioRepository {
        private final List<Usuario> usuarios = new ArrayList<>();

        void guardar(Usuario usuario) {
            usuarios.removeIf(u -> u.getId().equals(usuario.getId()));
            usuarios.add(usuario);
        }

        @Override
        public Usuario save(Usuario usuario) {
            guardar(usuario);
            return usuario;
        }

        @Override
        public Optional<Usuario> findById(Long id) {
            return usuarios.stream().filter(u -> id.equals(u.getId())).findFirst();
        }

        @Override
        public Optional<Usuario> findByCorreoElectronico(String email) {
            return Optional.empty();
        }

        @Override
        public List<Usuario> findByRol(RolUsuario rol) {
            return List.of();
        }

        @Override
        public List<Usuario> findActivos() {
            return List.of();
        }

        @Override
        public Pagina<Usuario> findAll(Paginacion paginacion) {
            return new Pagina<>(List.of(), 0, 0, 10);
        }

        @Override
        public boolean existsByCorreoElectronico(String email) {
            return false;
        }

        @Override
        public void deleteById(Long id) {
        }
    }

    private static final class AsignacionRepositoryFake implements AsignacionRepository {
        private final List<Asignacion> asignaciones = new ArrayList<>();

        void guardar(Asignacion asignacion) {
            asignaciones.add(asignacion);
        }

        @Override
        public Asignacion save(Asignacion asignacion) {
            guardar(asignacion);
            return asignacion;
        }

        @Override
        public Optional<Asignacion> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<Asignacion> findByIncidenciaId(Long incidenciaId) {
            return List.of();
        }

        @Override
        public Optional<Asignacion> findActivaByIncidenciaId(Long incidenciaId) {
            return Optional.empty();
        }

        @Override
        public List<Asignacion> findByTecnicoId(Long tecnicoId) {
            return asignaciones.stream()
                    .filter(a -> tecnicoId.equals(a.getTecnicoId()))
                    .toList();
        }

        @Override
        public void desactivarAsignacionesAnteriores(Long incidenciaId) {
        }
    }
}
