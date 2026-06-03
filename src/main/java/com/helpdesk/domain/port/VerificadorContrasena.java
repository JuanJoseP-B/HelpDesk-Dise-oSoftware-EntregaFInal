package com.helpdesk.domain.port;

/**
 * Puerto de dominio para verificación de contraseñas sin depender de Spring Security.
 */
public interface VerificadorContrasena {

    /**
     * Comprueba si la contraseña en texto plano coincide con el hash almacenado.
     *
     * @param contrasenaPlana contraseña sin cifrar
     * @param hashAlmacenado    hash persistido
     * @return true si coinciden
     */
    boolean coincide(String contrasenaPlana, String hashAlmacenado);
}
