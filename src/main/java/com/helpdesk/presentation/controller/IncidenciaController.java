package com.helpdesk.presentation.controller;

import com.helpdesk.application.command.*;
import com.helpdesk.application.dto.request.*;
import com.helpdesk.application.dto.response.ComentarioDTO;
import com.helpdesk.application.dto.response.HistorialEstadoDTO;
import com.helpdesk.application.dto.response.IncidenciaDetalleDTO;
import com.helpdesk.application.dto.response.IncidenciaResponseDTO;
import com.helpdesk.application.dto.response.IncidenciaSummaryDTO;
import com.helpdesk.application.exception.ResourceNotFoundException;
import com.helpdesk.application.usecase.ComentarioUseCase;
import com.helpdesk.application.usecase.IncidenciaUseCase;
import com.helpdesk.presentation.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para incidencias y transiciones de estado.
 */
@RestController
@RequestMapping("/incidencias")
public class IncidenciaController {

    private final IncidenciaUseCase incidenciaUseCase;
    private final ComentarioUseCase comentarioUseCase;

    public IncidenciaController(IncidenciaUseCase incidenciaUseCase, ComentarioUseCase comentarioUseCase) {
        this.incidenciaUseCase = incidenciaUseCase;
        this.comentarioUseCase = comentarioUseCase;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENTE','ADMINISTRADOR')")
    public ResponseEntity<IncidenciaResponseDTO> crear(@Valid @RequestBody CrearIncidenciaRequestDTO request) {
        IncidenciaResponseDTO response = incidenciaUseCase.crear(new CrearIncidenciaCommand(
                AuthenticatedUser.id(),
                request.titulo(),
                request.descripcion(),
                request.prioridad()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<IncidenciaSummaryDTO>> listarTodas(Pageable pageable) {
        return ResponseEntity.ok(incidenciaUseCase.listarTodas(pageable));
    }

    @GetMapping("/mis-tickets")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Page<IncidenciaSummaryDTO>> misTickets(Pageable pageable) {
        return ResponseEntity.ok(incidenciaUseCase.listarPorCliente(AuthenticatedUser.id(), pageable));
    }

    @GetMapping("/asignadas")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<Page<IncidenciaSummaryDTO>> asignadas(Pageable pageable) {
        return ResponseEntity.ok(incidenciaUseCase.listarPorTecnico(AuthenticatedUser.id(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenciaDetalleDTO> detalle(@PathVariable Long id) {
        return incidenciaUseCase.obtenerDetalle(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia", id));
    }

    @PutMapping("/{id}/asignar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<IncidenciaResponseDTO> asignar(
            @PathVariable Long id,
            @Valid @RequestBody AsignarTicketRequestDTO request
    ) {
        return ResponseEntity.ok(incidenciaUseCase.asignar(new AsignarTicketCommand(
                id,
                request.tecnicoId(),
                AuthenticatedUser.id(),
                request.motivo()
        )));
    }

    @PutMapping("/{id}/iniciar")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<IncidenciaResponseDTO> iniciar(@PathVariable Long id) {
        return ResponseEntity.ok(incidenciaUseCase.iniciarTrabajo(new IniciarTrabajoCommand(id, AuthenticatedUser.id())));
    }

    @PutMapping("/{id}/resolver")
    @PreAuthorize("hasRole('TECNICO')")
    public ResponseEntity<IncidenciaResponseDTO> resolver(
            @PathVariable Long id,
            @Valid @RequestBody ResolverTicketRequestDTO request
    ) {
        return ResponseEntity.ok(incidenciaUseCase.resolver(new ResolverTicketCommand(
                id,
                AuthenticatedUser.id(),
                request.solucion()
        )));
    }

    @PutMapping("/{id}/cerrar")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMINISTRADOR')")
    public ResponseEntity<IncidenciaResponseDTO> cerrar(@PathVariable Long id) {
        return ResponseEntity.ok(incidenciaUseCase.cerrar(new CerrarTicketCommand(id, AuthenticatedUser.id())));
    }

    @PutMapping("/{id}/reabrir")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<IncidenciaResponseDTO> reabrir(
            @PathVariable Long id,
            @Valid @RequestBody ReabrirTicketRequestDTO request
    ) {
        return ResponseEntity.ok(incidenciaUseCase.reabrir(new ReabrirTicketCommand(
                id,
                AuthenticatedUser.id(),
                request.motivo()
        )));
    }

    @PutMapping("/{id}/prioridad")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<IncidenciaResponseDTO> cambiarPrioridad(
            @PathVariable Long id,
            @Valid @RequestBody CambiarPrioridadRequestDTO request
    ) {
        return ResponseEntity.ok(incidenciaUseCase.cambiarPrioridad(new CambiarPrioridadCommand(
                id,
                AuthenticatedUser.id(),
                request.nuevaPrioridad(),
                request.motivo()
        )));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialEstadoDTO>> historial(@PathVariable Long id) {
        IncidenciaDetalleDTO detalle = incidenciaUseCase.obtenerDetalle(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia", id));
        return ResponseEntity.ok(detalle.historial());
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<ComentarioDTO>> comentarios(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioUseCase.listarPorIncidencia(id));
    }
}
