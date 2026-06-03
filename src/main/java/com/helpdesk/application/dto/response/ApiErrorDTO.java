package com.helpdesk.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Respuesta estandarizada de error de la API.
 */
public record ApiErrorDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> errors
) {
    public ApiErrorDTO(LocalDateTime timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, message, path, List.of());
    }
}
