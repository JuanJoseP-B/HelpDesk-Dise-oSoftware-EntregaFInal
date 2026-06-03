package com.helpdesk.application.mapper;

import com.helpdesk.application.dto.response.*;
import com.helpdesk.domain.entity.*;
import com.helpdesk.domain.pagination.Pagina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Mapeo entre entidades de dominio y DTOs de aplicación.
 */
public final class ApplicationMapper {

    private ApplicationMapper() {
    }

    public static IncidenciaResponseDTO toIncidenciaResponse(Incidencia incidencia) {
        return new IncidenciaResponseDTO(
                incidencia.getId(),
                incidencia.getTitulo(),
                incidencia.getDescripcion(),
                incidencia.getEstado(),
                incidencia.getNivelPrioridad(),
                incidencia.getFechaCreacion(),
                incidencia.getFechaAsignacion(),
                incidencia.getFechaResolucion(),
                incidencia.getFechaCierre(),
                incidencia.getClienteId(),
                incidencia.getTecnicoAsignadoId(),
                incidencia.isSlaViolado()
        );
    }

    public static IncidenciaSummaryDTO toIncidenciaSummary(Incidencia incidencia) {
        return new IncidenciaSummaryDTO(
                incidencia.getId(),
                incidencia.getTitulo(),
                incidencia.getEstado(),
                incidencia.getNivelPrioridad(),
                incidencia.getFechaCreacion()
        );
    }

    public static IncidenciaDetalleDTO toIncidenciaDetalle(
            Incidencia incidencia,
            List<HistorialEstadoDTO> historial,
            List<ComentarioDTO> comentarios
    ) {
        return new IncidenciaDetalleDTO(
                incidencia.getId(),
                incidencia.getTitulo(),
                incidencia.getEstado(),
                incidencia.getNivelPrioridad(),
                incidencia.getFechaCreacion(),
                incidencia.getDescripcion(),
                incidencia.getSolucion(),
                incidencia.getClienteId(),
                incidencia.getTecnicoAsignadoId(),
                incidencia.isSlaViolado(),
                historial,
                comentarios
        );
    }

    public static HistorialEstadoDTO toHistorialDto(HistorialEstado historial) {
        return new HistorialEstadoDTO(
                historial.getId(),
                historial.getEstadoAnterior(),
                historial.getEstadoNuevo(),
                historial.getFechaCambio(),
                historial.getUsuarioCambioId(),
                historial.getMotivo()
        );
    }

    public static ComentarioDTO toComentarioDto(Comentario comentario) {
        return new ComentarioDTO(
                comentario.getId(),
                comentario.getContenido(),
                comentario.getAutorId(),
                comentario.getFechaHora(),
                comentario.isVisibleParaCliente()
        );
    }

    public static UsuarioResponseDTO toUsuarioResponse(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreoElectronico(),
                usuario.getTelefono(),
                usuario.getRol(),
                usuario.isActivo(),
                usuario.getFechaRegistro()
        );
    }

    public static AcuerdoServicioDTO toAcuerdoDto(AcuerdoServicio acuerdo) {
        return new AcuerdoServicioDTO(
                acuerdo.getId(),
                acuerdo.getNombre(),
                acuerdo.getNivelPrioridad(),
                acuerdo.getTiempoMaxRespuestaHoras(),
                acuerdo.getTiempoMaxResolucionHoras(),
                acuerdo.isActivo()
        );
    }

    public static NotificacionDTO toNotificacionDto(Notificacion notificacion) {
        return new NotificacionDTO(
                notificacion.getId(),
                notificacion.getAsunto(),
                notificacion.getMensaje(),
                notificacion.getTipo(),
                notificacion.getEstadoEnvio(),
                notificacion.isLeida()
        );
    }

    public static <T, R> Page<R> toSpringPage(Pagina<T> pagina, Pageable pageable, java.util.function.Function<T, R> mapper) {
        List<R> content = pagina.contenido().stream().map(mapper).toList();
        return new PageImpl<>(content, pageable, pagina.totalElementos());
    }
}
