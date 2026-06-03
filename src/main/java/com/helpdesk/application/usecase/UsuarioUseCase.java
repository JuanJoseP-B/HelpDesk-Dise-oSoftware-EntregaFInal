package com.helpdesk.application.usecase;

import com.helpdesk.application.command.ActualizarUsuarioCommand;
import com.helpdesk.application.command.CambiarRolCommand;
import com.helpdesk.application.command.CrearUsuarioCommand;
import com.helpdesk.application.dto.response.UsuarioResponseDTO;
import com.helpdesk.application.exception.BusinessException;
import com.helpdesk.application.exception.ResourceNotFoundException;
import com.helpdesk.application.mapper.ApplicationMapper;
import com.helpdesk.application.util.PaginacionUtil;
import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.port.GeneradorContrasena;
import com.helpdesk.domain.repository.UsuarioRepository;
import com.helpdesk.domain.valueobject.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Casos de uso de gestión de usuarios (administración).
 */
@Service
public class UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final GeneradorContrasena generadorContrasena;

    public UsuarioUseCase(UsuarioRepository usuarioRepository, GeneradorContrasena generadorContrasena) {
        this.usuarioRepository = usuarioRepository;
        this.generadorContrasena = generadorContrasena;
    }

    @Transactional
    public UsuarioResponseDTO crear(CrearUsuarioCommand command) {
        String email = new Email(command.email()).getValor();
        if (usuarioRepository.existsByCorreoElectronico(email)) {
            throw new BusinessException("El correo electrónico ya existe");
        }

        Usuario usuario = new Usuario(
                null,
                command.nombre(),
                email,
                command.telefono(),
                generadorContrasena.generarHash(command.password()),
                true,
                command.rol(),
                LocalDateTime.now(),
                null
        );
        return ApplicationMapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO actualizar(ActualizarUsuarioCommand command) {
        Usuario usuario = obtenerUsuario(command.usuarioId());

        usuario.actualizarPerfil(command.nombre(), command.telefono());
        if (command.activo() != null) {
            if (command.activo()) {
                usuario.activar();
            } else {
                usuario.desactivar();
            }
        }

        return ApplicationMapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO cambiarRol(CambiarRolCommand command) {
        Usuario usuario = obtenerUsuario(command.usuarioId());
        usuario.cambiarRol(command.nuevoRol());
        return ApplicationMapper.toUsuarioResponse(usuarioRepository.save(usuario));
    }

    public Page<UsuarioResponseDTO> listarTodos(Pageable pageable) {
        var pagina = usuarioRepository.findAll(PaginacionUtil.fromPageable(pageable));
        return ApplicationMapper.toSpringPage(pagina, pageable, ApplicationMapper::toUsuarioResponse);
    }

    public Optional<UsuarioResponseDTO> obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(ApplicationMapper::toUsuarioResponse);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

}
