package com.helpdesk.application.usecase;

import com.helpdesk.application.dto.response.DashboardMetricsDTO;
import com.helpdesk.application.dto.response.SlaCumplimientoDTO;
import com.helpdesk.application.dto.response.TecnicoPerformanceDTO;
import com.helpdesk.domain.entity.Incidencia;
import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.repository.IncidenciaRepository;
import com.helpdesk.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Casos de uso de reportes y métricas para administradores.
 */
@Service
public class ReporteUseCase {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteUseCase(IncidenciaRepository incidenciaRepository, UsuarioRepository usuarioRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Obtiene métricas globales del dashboard.
     */
    public DashboardMetricsDTO obtenerDashboard() {
        List<Incidencia> todas = incidenciaRepository.findAll();

        long abiertas = incidenciaRepository.countByEstado(EstadoIncidencia.ABIERTO)
                + incidenciaRepository.countByEstado(EstadoIncidencia.ASIGNADO)
                + incidenciaRepository.countByEstado(EstadoIncidencia.EN_PROGRESO);
        long resueltas = incidenciaRepository.countByEstado(EstadoIncidencia.RESUELTO);
        long cerradas = incidenciaRepository.countByEstado(EstadoIncidencia.CERRADO);
        long slaViolados = todas.stream().filter(Incidencia::isSlaViolado).count();

        double promedioResolucion = todas.stream()
                .filter(i -> i.getFechaResolucion() != null && i.getFechaCreacion() != null)
                .mapToLong(i -> ChronoUnit.HOURS.between(i.getFechaCreacion(), i.getFechaResolucion()))
                .average()
                .orElse(0.0);

        return new DashboardMetricsDTO(
                todas.size(),
                abiertas,
                resueltas,
                cerradas,
                slaViolados,
                promedioResolucion
        );
    }

    /**
     * Calcula cumplimiento de SLA en un periodo.
     */
    public SlaCumplimientoDTO obtenerSlaCumplimiento(LocalDateTime desde, LocalDateTime hasta) {
        List<Incidencia> enPeriodo = incidenciaRepository.findAll().stream()
                .filter(i -> i.getFechaCreacion() != null)
                .filter(i -> !i.getFechaCreacion().isBefore(desde) && !i.getFechaCreacion().isAfter(hasta))
                .toList();

        long violados = enPeriodo.stream().filter(Incidencia::isSlaViolado).count();
        long total = enPeriodo.size();
        long cumplidos = total - violados;
        double porcentaje = total == 0 ? 100.0 : (cumplidos * 100.0) / total;

        String periodo = desde.toLocalDate() + " - " + hasta.toLocalDate();
        return new SlaCumplimientoDTO(periodo, total, cumplidos, violados, porcentaje);
    }

    /**
     * Obtiene rendimiento por técnico en el periodo indicado.
     */
    public List<TecnicoPerformanceDTO> obtenerRendimientoTecnicos(LocalDateTime desde, LocalDateTime hasta) {
        return usuarioRepository.findByRol(RolUsuario.TECNICO).stream()
                .map(tecnico -> calcularRendimiento(tecnico, desde, hasta))
                .toList();
    }

    /**
     * Cuenta incidencias por estado en un periodo.
     */
    public Map<String, Long> contarPorEstado(LocalDateTime desde, LocalDateTime hasta) {
        Map<String, Long> conteo = new HashMap<>();
        for (EstadoIncidencia estado : EstadoIncidencia.values()) {
            long count = incidenciaRepository.findAll().stream()
                    .filter(i -> i.getEstado() == estado)
                    .filter(i -> i.getFechaCreacion() != null)
                    .filter(i -> !i.getFechaCreacion().isBefore(desde) && !i.getFechaCreacion().isAfter(hasta))
                    .count();
            conteo.put(estado.name(), count);
        }
        return conteo;
    }

    private TecnicoPerformanceDTO calcularRendimiento(Usuario tecnico, LocalDateTime desde, LocalDateTime hasta) {
        List<Incidencia> asignadas = incidenciaRepository.findByTecnicoAsignadoId(
                tecnico.getId(),
                new com.helpdesk.domain.pagination.Paginacion(0, Integer.MAX_VALUE)
        ).contenido().stream()
                .filter(i -> i.getFechaCreacion() != null)
                .filter(i -> !i.getFechaCreacion().isBefore(desde) && !i.getFechaCreacion().isAfter(hasta))
                .toList();

        long resueltas = asignadas.stream()
                .filter(i -> i.getEstado() == EstadoIncidencia.RESUELTO
                        || i.getEstado() == EstadoIncidencia.CERRADO)
                .count();

        double promedioHoras = asignadas.stream()
                .filter(i -> i.getFechaResolucion() != null)
                .mapToLong(i -> ChronoUnit.HOURS.between(i.getFechaCreacion(), i.getFechaResolucion()))
                .average()
                .orElse(0.0);

        return new TecnicoPerformanceDTO(
                tecnico.getId(),
                tecnico.getNombre(),
                asignadas.size(),
                resueltas,
                promedioHoras
        );
    }
}
