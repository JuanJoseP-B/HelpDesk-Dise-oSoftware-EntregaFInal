package com.helpdesk.domain.port;

/**
 * Puerto para generar hash de contraseñas sin acoplar el dominio a BCrypt/Spring.
 */
public interface GeneradorContrasena {

    /**
     * Genera el hash seguro de una contraseña en texto plano.
     */
    String generarHash(String contrasenaPlana);
}
