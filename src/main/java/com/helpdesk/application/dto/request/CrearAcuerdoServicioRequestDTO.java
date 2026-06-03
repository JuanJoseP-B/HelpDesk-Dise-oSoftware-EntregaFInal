package com.helpdesk.application.dto.request;

import com.helpdesk.domain.enums.NivelPrioridad;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Solicitud de creación de acuerdo de servicio (SLA).
 */
public record CrearAcuerdoServicioRequestDTO(
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 500) String descripcion,
        @NotNull NivelPrioridad nivelPrioridad,
        @Min(1) int tiempoMaxRespuestaHoras,
        @Min(1) int tiempoMaxResolucionHoras
) {
}
