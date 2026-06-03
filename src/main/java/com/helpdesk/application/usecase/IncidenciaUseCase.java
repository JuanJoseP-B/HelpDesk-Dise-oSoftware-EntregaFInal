package com.helpdesk.application.usecase;

import com.helpdesk.application.command.*;
import com.helpdesk.application.dto.response.IncidenciaDetalleDTO;
import com.helpdesk.application.dto.response.IncidenciaResponseDTO;
import com.helpdesk.application.dto.response.IncidenciaSummaryDTO;
import com.helpdesk.application.exception.BusinessException;
import com.helpdesk.application.exception.ResourceNotFoundException;
import com.helpdesk.application.mapper.ApplicationMapper;
import com.helpdesk.application.service.IncidenciaQueryService;
import com.helpdesk.application.service.NotificacionApplicationService;
import com.helpdesk.application.util.PaginacionUtil;
import com.helpdesk.domain.entity.Asignacion;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.event.EstadoCambiadoEvent;
import com.helpdesk.domain.event.TicketAsignadoEvent;
import com.helpdesk.domain.event.TicketCreadoEvent;
import com.helpdesk.domain.event.TicketResueltoEvent;
import com.helpdesk.domain.port.DomainEventPublisher;
import com.helpdesk.domain.repository.AsignacionRepository;
import com.helpdesk.domain.repository.AcuerdoServicioRepository;
import com.helpdesk.domain.repository.IncidenciaRepository;
import com.helpdesk.domain.repository.UsuarioRepository;
import com.helpdesk.domain.service.AsignacionService;
import com.helpdesk.domain.service.SlaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Casos de uso de escritura y consulta de incidencias.
 */
@Service
public class IncidenciaUseCase {

    private final IncidenciaRepository incidenciaRepository;
    private final AcuerdoServicioRepository acuerdoServicioRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsignacionRepository asignacionRepository;
    private final AsignacionService asignacionService;
    private final SlaService slaService;
    private final DomainEventPublisher eventPublisher;
    private final NotificacionApplicationService notificacionApplicationService;
    private final IncidenciaQueryService incidenciaQueryService;

    public IncidenciaUseCase(
            IncidenciaRepository incidenciaRepository,
            AcuerdoServicioRepository acuerdoServicioRepository,
            UsuarioRepository usuarioRepository,
            AsignacionRepository asignacionRepository,
            AsignacionService asignacionService,
            SlaService slaService,
            DomainEventPublisher eventPublisher,
            NotificacionApplicationService notificacionApplicationService,
            IncidenciaQueryService incidenciaQueryService
    ) {
        this.incidenciaRepository = incidenciaRepository;
        this.acuerdoServicioRepository = acuerdoServicioRepository;
        this.usuarioRepository = usuarioRepository;
        this.asignacionRepository = asignacionRepository;
        this.asignacionService = asignacionService;
        this.slaService = slaService;
        this.eventPublisher = eventPublisher;
        this.notificacionApplicationService = notificacionApplicationService;
        this.incidenciaQueryService = incidenciaQueryService;
    }

    @Transactional
    public IncidenciaResponseDTO crear(CrearIncidenciaCommand command) {
        var acuerdo = acuerdoServicioRepository.findActivoByNivelPrioridad(command.prioridad())
                .orElseThrow(() -> new BusinessException(
                        "No existe acuerdo SLA activo para la prioridad " + command.prioridad()));

        LocalDateTime ahora = LocalDateTime.now();
        Incidencia incidencia = new Incidencia(
                null,
                command.titulo(),
                command.descripcion(),
                ahora,
                command.prioridad(),
                command.clienteId(),
                acuerdo.getId()
        );

        Incidencia guardada = incidenciaRepository.save(incidencia);

        TicketCreadoEvent evento = new TicketCreadoEvent(
                guardada.getId(),
                command.clienteId(),
                ahora,
                command.prioridad()
        );
        publicarEvento(evento);

        return ApplicationMapper.toIncidenciaResponse(guardada);
    }

    @Transactional
    public IncidenciaResponseDTO asignar(AsignarTicketCommand command) {
        Incidencia incidencia = incidenciaQueryService.obtenerIncidenciaOExcepcion(command.incidenciaId());
        RolUsuario rol = obtenerRol(command.asignadoPorId());

        asignacionService.validarDisponibilidadTecnico(command.tecnicoId());
        asignacionRepository.desactivarAsignacionesAnteriores(command.incidenciaId());

        LocalDateTime ahora = LocalDateTime.now();
        var estadoAntes = incidencia.getEstado();

        incidencia.asignar(command.tecnicoId(), ahora, rol);

        Asignacion asignacion = new Asignacion(
                null,
                command.incidenciaId(),
                command.tecnicoId(),
                command.asignadoPorId(),
                ahora,
                command.motivo(),
                true,
                false
        );
        asignacionRepository.save(asignacion);
        Incidencia guardada = incidenciaRepository.save(incidencia);

        TicketAsignadoEvent asignadoEvent = new TicketAsignadoEvent(
                guardada.getId(),
                command.tecnicoId(),
                command.asignadoPorId(),
                ahora
        );
        publicarEvento(asignadoEvent);

        if (estadoAntes != guardada.getEstado()) {
            EstadoCambiadoEvent cambioEvent = new EstadoCambiadoEvent(
                    guardada.getId(),
                    estadoAntes,
                    guardada.getEstado(),
                    command.asignadoPorId(),
                    ahora,
                    command.motivo()
            );
            publicarEvento(cambioEvent);
        }

        return ApplicationMapper.toIncidenciaResponse(guardada);
    }

    @Transactional
    public IncidenciaResponseDTO iniciarTrabajo(IniciarTrabajoCommand command) {
        Incidencia incidencia = incidenciaQueryService.obtenerIncidenciaOExcepcion(command.incidenciaId());
        validarTecnicoAsignado(incidencia, command.tecnicoId());

        var estadoAntes = incidencia.getEstado();
        incidencia.iniciarTrabajo(RolUsuario.TECNICO);
        Incidencia guardada = incidenciaRepository.save(incidencia);

        publicarCambioEstado(guardada, estadoAntes, command.tecnicoId(), "Trabajo iniciado");
        return ApplicationMapper.toIncidenciaResponse(guardada);
    }

    @Transactional
    public IncidenciaResponseDTO resolver(ResolverTicketCommand command) {
        Incidencia incidencia = incidenciaQueryService.obtenerIncidenciaOExcepcion(command.incidenciaId());
        validarTecnicoAsignado(incidencia, command.tecnicoId());

        var estadoAntes = incidencia.getEstado();
        LocalDateTime ahora = LocalDateTime.now();
        incidencia.resolver(command.solucion(), ahora, RolUsuario.TECNICO);
        Incidencia guardada = incidenciaRepository.save(incidencia);

        TicketResueltoEvent resueltoEvent = new TicketResueltoEvent(
                guardada.getId(),
                command.tecnicoId(),
                command.solucion(),
                ahora
        );
        publicarEvento(resueltoEvent);
        publicarCambioEstado(guardada, estadoAntes, command.tecnicoId(), "Resolución");

        return ApplicationMapper.toIncidenciaResponse(guardada);
    }

    @Transactional
    public IncidenciaResponseDTO cerrar(CerrarTicketCommand command) {
        Incidencia incidencia = incidenciaQueryService.obtenerIncidenciaOExcepcion(command.incidenciaId());
        RolUsuario rol = obtenerRol(command.usuarioId());

        var estadoAntes = incidencia.getEstado();
        LocalDateTime ahora = LocalDateTime.now();
        incidencia.cerrar(ahora, rol);
        Incidencia guardada = incidenciaRepository.save(incidencia);

        publicarCambioEstado(guardada, estadoAntes, command.usuarioId(), "Cierre");
        return ApplicationMapper.toIncidenciaResponse(guardada);
    }

    @Transactional
    public IncidenciaResponseDTO reabrir(ReabrirTicketCommand command) {
        Incidencia incidencia = incidenciaQueryService.obtenerIncidenciaOExcepcion(command.incidenciaId());
        if (!command.clienteId().equals(incidencia.getClienteId())) {
            throw new BusinessException("Solo el cliente propietario puede reabrir el ticket");
        }

        var estadoAntes = incidencia.getEstado();
        incidencia.reabrir(command.motivo(), RolUsuario.CLIENTE);
        Incidencia guardada = incidenciaRepository.save(incidencia);

        publicarCambioEstado(guardada, estadoAntes, command.clienteId(), command.motivo());
        return ApplicationMapper.toIncidenciaResponse(guardada);
    }

    @Transactional
    public IncidenciaResponseDTO cambiarPrioridad(CambiarPrioridadCommand command) {
        Incidencia incidencia = incidenciaQueryService.obtenerIncidenciaOExcepcion(command.incidenciaId());
        obtenerRol(command.adminId()); // valida existencia
        slaService.validarCambioPrioridad(incidencia, command.nuevaPrioridad());
        incidencia.escalarPrioridad(command.nuevaPrioridad(), RolUsuario.ADMINISTRADOR);
        Incidencia guardada = incidenciaRepository.save(incidencia);
        return ApplicationMapper.toIncidenciaResponse(guardada);
    }

    public Page<IncidenciaSummaryDTO> listarTodas(Pageable pageable) {
        var pagina = incidenciaQueryService.listarTodas(PaginacionUtil.fromPageable(pageable));
        return ApplicationMapper.toSpringPage(pagina, pageable, s -> s);
    }

    public Page<IncidenciaSummaryDTO> listarPorCliente(Long clienteId, Pageable pageable) {
        var pagina = incidenciaQueryService.listarPorCliente(clienteId, PaginacionUtil.fromPageable(pageable));
        return ApplicationMapper.toSpringPage(pagina, pageable, s -> s);
    }

    public Page<IncidenciaSummaryDTO> listarPorTecnico(Long tecnicoId, Pageable pageable) {
        var pagina = incidenciaQueryService.listarPorTecnico(tecnicoId, PaginacionUtil.fromPageable(pageable));
        return ApplicationMapper.toSpringPage(pagina, pageable, s -> s);
    }

    public Optional<IncidenciaDetalleDTO> obtenerDetalle(Long incidenciaId) {
        return incidenciaQueryService.obtenerDetalle(incidenciaId);
    }

    private void publicarCambioEstado(
            Incidencia incidencia,
            com.helpdesk.domain.enums.EstadoIncidencia estadoAntes,
            Long usuarioId,
            String motivo
    ) {
        if (estadoAntes == incidencia.getEstado()) {
            return;
        }
        EstadoCambiadoEvent evento = new EstadoCambiadoEvent(
                incidencia.getId(),
                estadoAntes,
                incidencia.getEstado(),
                usuarioId,
                LocalDateTime.now(),
                motivo
        );
        publicarEvento(evento);
    }

    private void publicarEvento(Object evento) {
        eventPublisher.publicar(evento);
    }

    private RolUsuario obtenerRol(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(Usuario::getRol)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));
    }

    private void validarTecnicoAsignado(Incidencia incidencia, Long tecnicoId) {
        if (!tecnicoId.equals(incidencia.getTecnicoAsignadoId())) {
            throw new BusinessException("El ticket no está asignado al técnico indicado");
        }
    }
}
