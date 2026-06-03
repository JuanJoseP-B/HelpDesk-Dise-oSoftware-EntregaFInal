package com.helpdesk.application.dto.response;

/**
 * Respuesta con tokens JWT de autenticación.
 */
public record JwtResponseDTO(
        String token,
        String refreshToken,
        String tipo,
        long expiracion
) {
}
