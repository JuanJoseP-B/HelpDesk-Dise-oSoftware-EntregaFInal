package com.helpdesk.presentation.controller;

import com.helpdesk.application.command.ActualizarUsuarioCommand;
import com.helpdesk.application.command.CambiarRolCommand;
import com.helpdesk.application.command.CrearUsuarioCommand;
import com.helpdesk.application.dto.request.ActualizarUsuarioRequestDTO;
import com.helpdesk.application.dto.request.CambiarRolRequestDTO;
import com.helpdesk.application.dto.request.CrearUsuarioRequestDTO;
import com.helpdesk.application.dto.response.UsuarioResponseDTO;
import com.helpdesk.application.exception.ResourceNotFoundException;
import com.helpdesk.application.usecase.UsuarioUseCase;
import com.helpdesk.presentation.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gestion de usuarios.
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Page<UsuarioResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(usuarioUseCase.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == T(com.helpdesk.presentation.security.AuthenticatedUser).id()")
    public ResponseEntity<UsuarioResponseDTO> obtener(@PathVariable Long id) {
        return usuarioUseCase.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody CrearUsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioUseCase.crear(new CrearUsuarioCommand(
                request.nombre(),
                request.email(),
                request.password(),
                request.telefono(),
                request.rol()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequestDTO request
    ) {
        return ResponseEntity.ok(usuarioUseCase.actualizar(new ActualizarUsuarioCommand(
                id,
                request.nombre(),
                request.telefono(),
                request.activo()
        )));
    }

    @PutMapping("/{id}/rol")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolRequestDTO request
    ) {
        return ResponseEntity.ok(usuarioUseCase.cambiarRol(new CambiarRolCommand(
                id,
                AuthenticatedUser.id(),
                request.nuevoRol()
        )));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
