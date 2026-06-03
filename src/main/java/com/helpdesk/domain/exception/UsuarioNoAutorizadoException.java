package com.helpdesk.domain.exception;

/**
 * Se lanza cuando un usuario no tiene permisos para realizar una operación.
 */
public class UsuarioNoAutorizadoException extends DomainException {

    public UsuarioNoAutorizadoException(String operacion) {
        super("Usuario no autorizado para: " + operacion);
    }
}
