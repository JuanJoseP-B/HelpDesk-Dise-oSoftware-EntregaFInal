package com.helpdesk.application.usecase;

import com.helpdesk.application.command.CrearIncidenciaCommand;
import com.helpdesk.application.dto.response.IncidenciaResponseDTO;
import com.helpdesk.application.exception.BusinessException;
import com.helpdesk.application.service.IncidenciaQueryService;
import com.helpdesk.application.service.NotificacionApplicationService;
import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.event.TicketCreadoEvent;
import com.helpdesk.domain.port.DomainEventPublisher;
import com.helpdesk.domain.repository.AsignacionRepository;
import com.helpdesk.domain.repository.AcuerdoServicioRepository;
import com.helpdesk.domain.repository.ComentarioRepository;
import com.helpdesk.domain.repository.HistorialEstadoRepository;
import com.helpdesk.domain.repository.IncidenciaRepository;
import com.helpdesk.domain.repository.NotificacionRepository;
import com.helpdesk.domain.repository.UsuarioRepository;
import com.helpdesk.domain.service.AsignacionService;
import com.helpdesk.domain.service.SlaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IncidenciaUseCaseTest {

    private IncidenciaRepositoryFake incidenciaRepository;
    private AcuerdoServicioRepositoryFake acuerdoRepository;
    private AtomicReference<Object> ultimoEvento;
    private IncidenciaUseCase incidenciaUseCase;

    @BeforeEach
    void setUp() {
        incidenciaRepository = new IncidenciaRepositoryFake();
        acuerdoRepository = new AcuerdoServicioRepositoryFake();
        ultimoEvento = new AtomicReference<>();

        DomainEventPublisher publisher = ultimoEvento::set;
        NotificacionRepositoryFake notificacionRepo = new NotificacionRepositoryFake();
        NotificacionApplicationService notificacionService = new NotificacionApplicationService(notificacionRepo);

        IncidenciaQueryService queryService = new IncidenciaQueryService(
                incidenciaRepository,
                new HistorialEstadoRepositoryFake(),
                new ComentarioRepositoryFake()
        );

        incidenciaUseCase = new IncidenciaUseCase(
                incidenciaRepository,
                acuerdoRepository,
                new UsuarioRepositoryFake(),
                new AsignacionRepositoryFake(),
                new AsignacionService(new UsuarioRepositoryFake(), new AsignacionRepositoryFake()),
                new SlaService(),
                publisher,
                notificacionService,
                queryService
        );
    }

    @Test
    void crearIncidenciaPublicaEvento() {
        acuerdoRepository.guardar(new AcuerdoServicio(
                1L, "SLA Media", "Desc", NivelPrioridad.MEDIA, 8, 48, true, LocalDateTime.now()
        ));

        IncidenciaResponseDTO response = incidenciaUseCase.crear(
                new CrearIncidenciaCommand(100L, "Fallo red", "Sin conexión", NivelPrioridad.MEDIA)
        );

        assertEquals("Fallo red", response.titulo());
        assertNotNull(ultimoEvento.get());
        assertEquals(TicketCreadoEvent.class, ultimoEvento.get().getClass());
    }

    @Test
    void crearSinSlaActivoLanzaExcepcion() {
        assertThrows(BusinessException.class, () -> incidenciaUseCase.crear(
                new CrearIncidenciaCommand(1L, "T", "D", NivelPrioridad.ALTA)
        ));
    }

    private static final class IncidenciaRepositoryFake implements IncidenciaRepository {
        private final List<Incidencia> store = new ArrayList<>();
        private long seq = 1;

        @Override
        public Incidencia save(Incidencia incidencia) {
            if (incidencia.getId() == null) {
                incidencia.setId(seq++);
            }
            store.removeIf(i -> i.getId().equals(incidencia.getId()));
            store.add(incidencia);
            return incidencia;
        }

        @Override
        public Optional<Incidencia> findById(Long id) {
            return store.stream().filter(i -> id.equals(i.getId())).findFirst();
        }

        @Override
        public List<Incidencia> findAll() {
            return List.copyOf(store);
        }

        @Override
        public com.helpdesk.domain.pagination.Pagina<Incidencia> findByEstado(
                com.helpdesk.domain.enums.EstadoIncidencia estado,
                com.helpdesk.domain.pagination.Paginacion paginacion
        ) {
            return new com.helpdesk.domain.pagination.Pagina<>(List.of(), 0, 0, 10);
        }

        @Override
        public com.helpdesk.domain.pagination.Pagina<Incidencia> findByClienteId(
                Long clienteId, com.helpdesk.domain.pagination.Paginacion paginacion
        ) {
            return new com.helpdesk.domain.pagination.Pagina<>(List.of(), 0, 0, 10);
        }

        @Override
        public com.helpdesk.domain.pagination.Pagina<Incidencia> findByTecnicoAsignadoId(
                Long tecnicoId, com.helpdesk.domain.pagination.Paginacion paginacion
        ) {
            return new com.helpdesk.domain.pagination.Pagina<>(List.of(), 0, 0, 10);
        }

        @Override
        public List<Incidencia> findByEstadoAndFechaCreacionBefore(
                com.helpdesk.domain.enums.EstadoIncidencia estado, LocalDateTime fecha
        ) {
            return List.of();
        }

        @Override
        public List<Incidencia> findVencidas(LocalDateTime ahora) {
            return List.of();
        }

        @Override
        public boolean existsById(Long id) {
            return findById(id).isPresent();
        }

        @Override
        public long countByEstado(com.helpdesk.domain.enums.EstadoIncidencia estado) {
            return 0;
        }

        @Override
        public void deleteById(Long id) {
            store.removeIf(i -> id.equals(i.getId()));
        }
    }

    private static final class AcuerdoServicioRepositoryFake implements AcuerdoServicioRepository {
        private final List<AcuerdoServicio> store = new ArrayList<>();

        void guardar(AcuerdoServicio acuerdo) {
            store.add(acuerdo);
        }

        @Override
        public AcuerdoServicio save(AcuerdoServicio acuerdo) {
            guardar(acuerdo);
            return acuerdo;
        }

        @Override
        public Optional<AcuerdoServicio> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<AcuerdoServicio> findAllActivos() {
            return store.stream().filter(AcuerdoServicio::estaActivo).toList();
        }

        @Override
        public Optional<AcuerdoServicio> findActivoByNivelPrioridad(NivelPrioridad prioridad) {
            return store.stream()
                    .filter(a -> a.getNivelPrioridad() == prioridad && a.estaActivo())
                    .findFirst();
        }

        @Override
        public void deleteById(Long id) {
        }
    }

    private static final class UsuarioRepositoryFake implements UsuarioRepository {
        @Override
        public com.helpdesk.domain.entity.Usuario save(com.helpdesk.domain.entity.Usuario usuario) {
            return usuario;
        }

        @Override
        public Optional<com.helpdesk.domain.entity.Usuario> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public Optional<com.helpdesk.domain.entity.Usuario> findByCorreoElectronico(String email) {
            return Optional.empty();
        }

        @Override
        public List<com.helpdesk.domain.entity.Usuario> findByRol(com.helpdesk.domain.enums.RolUsuario rol) {
            return List.of();
        }

        @Override
        public List<com.helpdesk.domain.entity.Usuario> findActivos() {
            return List.of();
        }

        @Override
        public com.helpdesk.domain.pagination.Pagina<com.helpdesk.domain.entity.Usuario> findAll(
                com.helpdesk.domain.pagination.Paginacion paginacion
        ) {
            return new com.helpdesk.domain.pagination.Pagina<>(List.of(), 0, 0, 10);
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
        @Override
        public com.helpdesk.domain.entity.Asignacion save(com.helpdesk.domain.entity.Asignacion asignacion) {
            return asignacion;
        }

        @Override
        public Optional<com.helpdesk.domain.entity.Asignacion> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<com.helpdesk.domain.entity.Asignacion> findByIncidenciaId(Long incidenciaId) {
            return List.of();
        }

        @Override
        public Optional<com.helpdesk.domain.entity.Asignacion> findActivaByIncidenciaId(Long incidenciaId) {
            return Optional.empty();
        }

        @Override
        public List<com.helpdesk.domain.entity.Asignacion> findByTecnicoId(Long tecnicoId) {
            return List.of();
        }

        @Override
        public void desactivarAsignacionesAnteriores(Long incidenciaId) {
        }
    }

    private static final class HistorialEstadoRepositoryFake implements HistorialEstadoRepository {
        @Override
        public com.helpdesk.domain.entity.HistorialEstado save(
                com.helpdesk.domain.entity.HistorialEstado historial
        ) {
            return historial;
        }

        @Override
        public List<com.helpdesk.domain.entity.HistorialEstado> findByIncidenciaIdOrderByFechaCambioDesc(
                Long incidenciaId
        ) {
            return List.of();
        }

        @Override
        public Optional<com.helpdesk.domain.entity.HistorialEstado> findUltimoByIncidenciaId(Long incidenciaId) {
            return Optional.empty();
        }
    }

    private static final class ComentarioRepositoryFake implements ComentarioRepository {
        @Override
        public com.helpdesk.domain.entity.Comentario save(com.helpdesk.domain.entity.Comentario comentario) {
            return comentario;
        }

        @Override
        public Optional<com.helpdesk.domain.entity.Comentario> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<com.helpdesk.domain.entity.Comentario> findByIncidenciaId(Long incidenciaId) {
            return List.of();
        }

        @Override
        public List<com.helpdesk.domain.entity.Comentario> findByIncidenciaIdAndVisibleParaClienteTrue(
                Long incidenciaId
        ) {
            return List.of();
        }

        @Override
        public void deleteById(Long id) {
        }
    }

    private static final class NotificacionRepositoryFake implements NotificacionRepository {
        @Override
        public com.helpdesk.domain.entity.Notificacion save(com.helpdesk.domain.entity.Notificacion notificacion) {
            return notificacion;
        }

        @Override
        public List<com.helpdesk.domain.entity.Notificacion> findPendientes() {
            return List.of();
        }

        @Override
        public List<com.helpdesk.domain.entity.Notificacion> findByDestinatarioId(Long destinatarioId) {
            return List.of();
        }

        @Override
        public void marcarEnviado(Long notificacionId) {
        }

        @Override
        public void marcarFallido(Long notificacionId, String error) {
        }
    }
}
