package com.helpdesk.presentation.controller;

import com.helpdesk.application.dto.response.DashboardMetricsDTO;
import com.helpdesk.application.dto.response.SlaCumplimientoDTO;
import com.helpdesk.application.dto.response.TecnicoPerformanceDTO;
import com.helpdesk.application.usecase.ReporteUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para reportes administrativos.
 */
@RestController
@RequestMapping("/reportes")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ReporteController {

    private final ReporteUseCase reporteUseCase;

    public ReporteController(ReporteUseCase reporteUseCase) {
        this.reporteUseCase = reporteUseCase;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardMetricsDTO> dashboard() {
        return ResponseEntity.ok(reporteUseCase.obtenerDashboard());
    }

    @GetMapping("/sla-cumplimiento")
    public ResponseEntity<SlaCumplimientoDTO> slaCumplimiento(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        Periodo periodo = periodo(desde, hasta);
        return ResponseEntity.ok(reporteUseCase.obtenerSlaCumplimiento(periodo.desde(), periodo.hasta()));
    }

    @GetMapping("/tecnico-rendimiento")
    public ResponseEntity<List<TecnicoPerformanceDTO>> tecnicoRendimiento(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        Periodo periodo = periodo(desde, hasta);
        return ResponseEntity.ok(reporteUseCase.obtenerRendimientoTecnicos(periodo.desde(), periodo.hasta()));
    }

    @GetMapping("/incidencias-por-estado")
    public ResponseEntity<Map<String, Long>> incidenciasPorEstado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        Periodo periodo = periodo(desde, hasta);
        return ResponseEntity.ok(reporteUseCase.contarPorEstado(periodo.desde(), periodo.hasta()));
    }

    private Periodo periodo(LocalDateTime desde, LocalDateTime hasta) {
        LocalDateTime fin = hasta != null ? hasta : LocalDateTime.now();
        LocalDateTime inicio = desde != null ? desde : fin.minusDays(30);
        return new Periodo(inicio, fin);
    }

    private record Periodo(LocalDateTime desde, LocalDateTime hasta) {
    }
}
