package com.helpdesk.infrastructure.security;

import com.helpdesk.domain.port.GeneradorContrasena;
import com.helpdesk.domain.port.VerificadorContrasena;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adaptador BCrypt para hash y verificacion de contrasenas.
 */
@Component
public class BCryptPasswordAdapter implements GeneradorContrasena, VerificadorContrasena {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String generarHash(String contrasenaPlana) {
        return passwordEncoder.encode(contrasenaPlana);
    }

    @Override
    public boolean coincide(String contrasenaPlana, String hashAlmacenado) {
        return passwordEncoder.matches(contrasenaPlana, hashAlmacenado);
    }
}
