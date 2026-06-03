package com.helpdesk.domain.valueobject;

import com.helpdesk.domain.exception.DomainException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object inmutable para direcciones de correo electrónico.
 */
public final class Email {

    private static final Pattern PATRON_EMAIL = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String valor;

    public Email(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new DomainException("El email no puede estar vacío");
        }
        String normalizado = valor.trim().toLowerCase();
        if (!PATRON_EMAIL.matcher(normalizado).matches()) {
            throw new DomainException("Formato de email inválido: " + valor);
        }
        this.valor = normalizado;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email = (Email) o;
        return Objects.equals(valor, email.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
