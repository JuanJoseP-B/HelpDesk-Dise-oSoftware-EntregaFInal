package com.helpdesk.application.port;

import com.helpdesk.domain.entity.Usuario;

/**
 * Puerto de aplicación para generación y renovación de tokens JWT.
 */
public interface JwtService {

    /**
     * Genera un par de tokens para el usuario autenticado.
     */
    TokenPar generarTokens(Usuario usuario);

    /**
     * Renueva el access token a partir de un refresh token válido.
     */
    TokenPar renovar(String refreshToken);

    /**
     * Par de tokens de autenticación.
     */
    record TokenPar(String accessToken, String refreshToken, long expiracionMs) {
    }
}
