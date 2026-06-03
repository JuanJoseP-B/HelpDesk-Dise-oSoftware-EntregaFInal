package com.helpdesk.application.service;

import com.helpdesk.application.dto.response.ComentarioDTO;
import com.helpdesk.application.dto.response.HistorialEstadoDTO;
import com.helpdesk.application.dto.response.IncidenciaDetalleDTO;
import com.helpdesk.application.dto.response.IncidenciaSummaryDTO;
import com.helpdesk.application.exception.ResourceNotFoundException;
import com.helpdesk.application.mapper.ApplicationMapper;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.pagination.Pagina;
import com.helpdesk.domain.pagination.Paginacion;
import com.helpdesk.domain.repository.ComentarioRepository;
import com.helpdesk.domain.repository.HistorialEstadoRepository;
import com.helpdesk.domain.repository.IncidenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de lectura optimizado para consultas de incidencias (lado Query en CQRS).
 */
@Service
public class IncidenciaQueryService {

    private final IncidenciaRepository incidenciaRepository;
    private final HistorialEstadoRepository historialEstadoRepository;
    private final ComentarioRepository comentarioRepository;

    public IncidenciaQueryService(
            IncidenciaRepository incidenciaRepository,
            HistorialEstadoRepository historialEstadoRepository,
            ComentarioRepository comentarioRepository
    ) {
        this.incidenciaRepository = incidenciaRepository;
        this.historialEstadoRepository = historialEstadoRepository;
        this.comentarioRepository = comentarioRepository;
    }

    /**
     * Lista todas las incidencias paginadas (vista administrador).
     */
    public Pagina<IncidenciaSummaryDTO> listarTodas(Paginacion paginacion) {
        return paginarResumen(incidenciaRepository.findAll(), paginacion);
    }

    /**
     * Lista incidencias de un cliente.
     */
    public Pagina<IncidenciaSummaryDTO> listarPorCliente(Long clienteId, Paginacion paginacion) {
        Pagina<Incidencia> pagina = incidenciaRepository.findByClienteId(clienteId, paginacion);
        return mapearPaginaResumen(pagina);
    }

    /**
     * Lista incidencias asignadas a un técnico.
     */
    public Pagina<IncidenciaSummaryDTO> listarPorTecnico(Long tecnicoId, Paginacion paginacion) {
        Pagina<Incidencia> pagina = incidenciaRepository.findByTecnicoAsignadoId(tecnicoId, paginacion);
        return mapearPaginaResumen(pagina);
    }

    /**
     * Obtiene el detalle completo de una incidencia.
     */
    public Optional<IncidenciaDetalleDTO> obtenerDetalle(Long incidenciaId) {
        return incidenciaRepository.findById(incidenciaId)
                .map(this::construirDetalle);
    }

    public Incidencia obtenerIncidenciaOExcepcion(Long incidenciaId) {
        return incidenciaRepository.findById(incidenciaId)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia", incidenciaId));
    }

    private IncidenciaDetalleDTO construirDetalle(Incidencia incidencia) {
        List<HistorialEstadoDTO> historial = historialEstadoRepository
                .findByIncidenciaIdOrderByFechaCambioDesc(incidencia.getId())
                .stream()
                .map(ApplicationMapper::toHistorialDto)
                .toList();

        List<ComentarioDTO> comentarios = comentarioRepository.findByIncidenciaId(incidencia.getId())
                .stream()
                .map(ApplicationMapper::toComentarioDto)
                .toList();

        return ApplicationMapper.toIncidenciaDetalle(incidencia, historial, comentarios);
    }

    private Pagina<IncidenciaSummaryDTO> paginarResumen(List<Incidencia> todas, Paginacion paginacion) {
        int from = paginacion.getOffset();
        int to = Math.min(from + paginacion.tamanoPagina(), todas.size());
        List<IncidenciaSummaryDTO> contenido = todas.subList(
                Math.min(from, todas.size()),
                to
        ).stream().map(ApplicationMapper::toIncidenciaSummary).toList();
        return new Pagina<>(contenido, todas.size(), paginacion.numeroPagina(), paginacion.tamanoPagina());
    }

    private Pagina<IncidenciaSummaryDTO> mapearPaginaResumen(Pagina<Incidencia> pagina) {
        List<IncidenciaSummaryDTO> contenido = pagina.contenido().stream()
                .map(ApplicationMapper::toIncidenciaSummary)
                .toList();
        return new Pagina<>(contenido, pagina.totalElementos(), pagina.numeroPagina(), pagina.tamanoPagina());
    }
}
