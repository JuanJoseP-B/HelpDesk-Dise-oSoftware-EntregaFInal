package com.helpdesk.application.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Solicitud de renovacion de token JWT.
 */
public record RefreshTokenRequestDTO(
        @NotBlank String refreshToken
) {
}
