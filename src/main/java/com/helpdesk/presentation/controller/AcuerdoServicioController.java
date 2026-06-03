package com.helpdesk.presentation.controller;

import com.helpdesk.application.command.CrearAcuerdoServicioCommand;
import com.helpdesk.application.dto.request.CrearAcuerdoServicioRequestDTO;
import com.helpdesk.application.dto.response.AcuerdoServicioDTO;
import com.helpdesk.application.usecase.AcuerdoServicioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para acuerdos de servicio.
 */
@RestController
@RequestMapping("/slas")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AcuerdoServicioController {

    private final AcuerdoServicioUseCase acuerdoServicioUseCase;

    public AcuerdoServicioController(AcuerdoServicioUseCase acuerdoServicioUseCase) {
        this.acuerdoServicioUseCase = acuerdoServicioUseCase;
    }

    @GetMapping
    public ResponseEntity<List<AcuerdoServicioDTO>> listar() {
        return ResponseEntity.ok(acuerdoServicioUseCase.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcuerdoServicioDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(acuerdoServicioUseCase.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AcuerdoServicioDTO> crear(@Valid @RequestBody CrearAcuerdoServicioRequestDTO request) {
        AcuerdoServicioDTO response = acuerdoServicioUseCase.crear(new CrearAcuerdoServicioCommand(
                request.nombre(),
                request.descripcion(),
                request.nivelPrioridad(),
                request.tiempoMaxRespuestaHoras(),
                request.tiempoMaxResolucionHoras()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AcuerdoServicioDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CrearAcuerdoServicioRequestDTO request
    ) {
        return ResponseEntity.ok(acuerdoServicioUseCase.actualizar(
                id,
                new CrearAcuerdoServicioCommand(
                        request.nombre(),
                        request.descripcion(),
                        request.nivelPrioridad(),
                        request.tiempoMaxRespuestaHoras(),
                        request.tiempoMaxResolucionHoras()
                )
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        acuerdoServicioUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
