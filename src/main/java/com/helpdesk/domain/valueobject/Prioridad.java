package com.helpdesk.domain.valueobject;

import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.exception.DomainException;

import java.util.Objects;

/**
 * Value Object que encapsula el nivel de prioridad con validaciones de negocio.
 */
public final class Prioridad {

    private final NivelPrioridad nivel;

    public Prioridad(NivelPrioridad nivel) {
        if (nivel == null) {
            throw new DomainException("El nivel de prioridad es obligatorio");
        }
        this.nivel = nivel;
    }

    public static Prioridad de(NivelPrioridad nivel) {
        return new Prioridad(nivel);
    }

    public NivelPrioridad getNivel() {
        return nivel;
    }

    public int getHorasMaxRespuesta() {
        return nivel.getHorasMaxRespuesta();
    }

    public int getHorasMaxResolucion() {
        return nivel.getHorasMaxResolucion();
    }

    public boolean esMasUrgenteQue(Prioridad otra) {
        return this.nivel.ordinal() > otra.nivel.ordinal();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prioridad prioridad = (Prioridad) o;
        return nivel == prioridad.nivel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nivel);
    }

    @Override
    public String toString() {
        return nivel.name();
    }
}
