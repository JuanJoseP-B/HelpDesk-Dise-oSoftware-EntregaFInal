package com.helpdesk.presentation.security;

import com.helpdesk.infrastructure.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilidad para leer el usuario autenticado desde Spring Security.
 */
public final class AuthenticatedUser {

    private AuthenticatedUser() {
    }

    public static Long id() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new IllegalStateException("No hay usuario autenticado");
        }
        return userDetails.getUsuarioId();
    }
}
