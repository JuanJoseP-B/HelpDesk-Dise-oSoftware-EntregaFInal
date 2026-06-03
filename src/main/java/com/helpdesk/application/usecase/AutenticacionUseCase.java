package com.helpdesk.application.usecase;

import com.helpdesk.application.dto.request.LoginRequestDTO;
import com.helpdesk.application.dto.request.RegistroRequestDTO;
import com.helpdesk.application.dto.response.JwtResponseDTO;
import com.helpdesk.application.dto.response.UsuarioResponseDTO;
import com.helpdesk.application.exception.BusinessException;
import com.helpdesk.application.mapper.ApplicationMapper;
import com.helpdesk.application.port.JwtService;
import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.port.GeneradorContrasena;
import com.helpdesk.domain.port.VerificadorContrasena;
import com.helpdesk.domain.repository.UsuarioRepository;
import com.helpdesk.domain.valueobject.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Casos de uso de autenticación y registro.
 */
@Service
public class AutenticacionUseCase {

    private static final String TIPO_BEARER = "Bearer";

    private final UsuarioRepository usuarioRepository;
    private final VerificadorContrasena verificadorContrasena;
    private final GeneradorContrasena generadorContrasena;
    private final JwtService jwtService;

    public AutenticacionUseCase(
            UsuarioRepository usuarioRepository,
            VerificadorContrasena verificadorContrasena,
            GeneradorContrasena generadorContrasena,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.verificadorContrasena = verificadorContrasena;
        this.generadorContrasena = generadorContrasena;
        this.jwtService = jwtService;
    }

    public JwtResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByCorreoElectronico(request.email().toLowerCase())
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!usuario.validarCredenciales(request.password(), verificadorContrasena)) {
            throw new BusinessException("Credenciales inválidas");
        }

        usuario.actualizarUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        return toJwtResponse(jwtService.generarTokens(usuario));
    }

    public JwtResponseDTO refresh(String refreshToken) {
        return toJwtResponse(jwtService.renovar(refreshToken));
    }

    @Transactional
    public UsuarioResponseDTO registrar(RegistroRequestDTO request) {
        String emailNormalizado = new Email(request.email()).getValor();

        if (usuarioRepository.existsByCorreoElectronico(emailNormalizado)) {
            throw new BusinessException("El correo electrónico ya está registrado");
        }

        Usuario usuario = new Usuario(
                null,
                request.nombre(),
                emailNormalizado,
                request.telefono(),
                generadorContrasena.generarHash(request.password()),
                true,
                RolUsuario.CLIENTE,
                LocalDateTime.now(),
                null
        );

        Usuario guardado = usuarioRepository.save(usuario);
        return ApplicationMapper.toUsuarioResponse(guardado);
    }

    private JwtResponseDTO toJwtResponse(JwtService.TokenPar tokens) {
        return new JwtResponseDTO(
                tokens.accessToken(),
                tokens.refreshToken(),
                TIPO_BEARER,
                tokens.expiracionMs()
        );
    }
}
