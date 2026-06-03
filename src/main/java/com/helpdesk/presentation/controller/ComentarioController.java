package com.helpdesk.presentation.controller;

import com.helpdesk.application.command.CrearComentarioCommand;
import com.helpdesk.application.dto.request.CrearComentarioRequestDTO;
import com.helpdesk.application.dto.request.EditarComentarioRequestDTO;
import com.helpdesk.application.dto.response.ComentarioDTO;
import com.helpdesk.application.usecase.ComentarioUseCase;
import com.helpdesk.presentation.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para comentarios de incidencias.
 */
@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioUseCase comentarioUseCase;

    public ComentarioController(ComentarioUseCase comentarioUseCase) {
        this.comentarioUseCase = comentarioUseCase;
    }

    @PostMapping("/incidencias/{id}/comentarios")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMINISTRADOR')")
    public ResponseEntity<ComentarioDTO> agregar(
            @PathVariable Long id,
            @Valid @RequestBody CrearComentarioRequestDTO request
    ) {
        ComentarioDTO response = comentarioUseCase.agregar(new CrearComentarioCommand(
                id,
                AuthenticatedUser.id(),
                request.contenido(),
                request.visibleCliente()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMINISTRADOR')")
    public ResponseEntity<ComentarioDTO> editar(
            @PathVariable Long id,
            @Valid @RequestBody EditarComentarioRequestDTO request
    ) {
        return ResponseEntity.ok(comentarioUseCase.editar(id, request, AuthenticatedUser.id()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comentarioUseCase.eliminar(id, AuthenticatedUser.id());
        return ResponseEntity.noContent().build();
    }
}
