package com.helpdesk.application.usecase;

import com.helpdesk.application.command.CrearComentarioCommand;
import com.helpdesk.application.dto.request.EditarComentarioRequestDTO;
import com.helpdesk.application.dto.response.ComentarioDTO;
import com.helpdesk.application.exception.BusinessException;
import com.helpdesk.application.exception.ResourceNotFoundException;
import com.helpdesk.application.mapper.ApplicationMapper;
import com.helpdesk.application.service.IncidenciaQueryService;
import com.helpdesk.application.service.NotificacionApplicationService;
import com.helpdesk.domain.entity.Comentario;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.repository.ComentarioRepository;
import com.helpdesk.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Casos de uso de comentarios en incidencias.
 */
@Service
public class ComentarioUseCase {

    private final ComentarioRepository comentarioRepository;
    private final IncidenciaQueryService incidenciaQueryService;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionApplicationService notificacionApplicationService;

    public ComentarioUseCase(
            ComentarioRepository comentarioRepository,
            IncidenciaQueryService incidenciaQueryService,
            UsuarioRepository usuarioRepository,
            NotificacionApplicationService notificacionApplicationService
    ) {
        this.comentarioRepository = comentarioRepository;
        this.incidenciaQueryService = incidenciaQueryService;
        this.usuarioRepository = usuarioRepository;
        this.notificacionApplicationService = notificacionApplicationService;
    }

    @Transactional
    public ComentarioDTO agregar(CrearComentarioCommand command) {
        Incidencia incidencia = incidenciaQueryService.obtenerIncidenciaOExcepcion(command.incidenciaId());
        usuarioRepository.findById(command.autorId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", command.autorId()));

        Comentario comentario = new Comentario(
                null,
                command.incidenciaId(),
                command.autorId(),
                command.contenido(),
                LocalDateTime.now(),
                command.visibleCliente()
        );

        incidencia.agregarComentario(comentario);
        Comentario guardado = comentarioRepository.save(comentario);

        Long destinatario = incidencia.getClienteId().equals(command.autorId())
                ? incidencia.getTecnicoAsignadoId()
                : incidencia.getClienteId();
        if (destinatario != null) {
            notificacionApplicationService.onComentarioNuevo(destinatario, incidencia.getId());
        }

        return ApplicationMapper.toComentarioDto(guardado);
    }

    @Transactional
    public ComentarioDTO editar(Long comentarioId, EditarComentarioRequestDTO dto, Long autorId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", comentarioId));

        if (!comentario.getAutorId().equals(autorId)) {
            throw new BusinessException("Solo el autor puede editar el comentario");
        }

        comentario.editar(dto.contenido());
        return ApplicationMapper.toComentarioDto(comentarioRepository.save(comentario));
    }

    @Transactional
    public void eliminar(Long comentarioId, Long solicitanteId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Comentario", comentarioId));

        var solicitante = usuarioRepository.findById(solicitanteId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", solicitanteId));

        boolean esAutor = comentario.getAutorId().equals(solicitanteId);
        boolean esAdmin = solicitante.getRol() == RolUsuario.ADMINISTRADOR;

        if (!esAutor && !esAdmin) {
            throw new BusinessException("No tiene permisos para eliminar el comentario");
        }

        comentarioRepository.deleteById(comentarioId);
    }

    public List<ComentarioDTO> listarPorIncidencia(Long incidenciaId) {
        incidenciaQueryService.obtenerIncidenciaOExcepcion(incidenciaId);
        return comentarioRepository.findByIncidenciaId(incidenciaId).stream()
                .map(ApplicationMapper::toComentarioDto)
                .toList();
    }
}
