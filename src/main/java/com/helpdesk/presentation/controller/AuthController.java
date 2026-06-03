package com.helpdesk.presentation.controller;

import com.helpdesk.application.dto.request.LoginRequestDTO;
import com.helpdesk.application.dto.request.RefreshTokenRequestDTO;
import com.helpdesk.application.dto.request.RegistroRequestDTO;
import com.helpdesk.application.dto.response.JwtResponseDTO;
import com.helpdesk.application.dto.response.UsuarioResponseDTO;
import com.helpdesk.application.usecase.AutenticacionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller REST para autenticacion y registro.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutenticacionUseCase autenticacionUseCase;

    public AuthController(AutenticacionUseCase autenticacionUseCase) {
        this.autenticacionUseCase = autenticacionUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(autenticacionUseCase.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@Valid @RequestBody RegistroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autenticacionUseCase.registrar(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        return ResponseEntity.ok(autenticacionUseCase.refresh(request.refreshToken()));
    }
}
