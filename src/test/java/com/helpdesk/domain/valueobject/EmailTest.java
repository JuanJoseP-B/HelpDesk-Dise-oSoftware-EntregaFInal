package com.helpdesk.domain.valueobject;

import com.helpdesk.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailTest {

    @Test
    void creaEmailValido() {
        Email email = new Email("Usuario@HelpDesk.COM");
        assertEquals("usuario@helpdesk.com", email.getValor());
    }

    @Test
    void rechazaEmailInvalido() {
        assertThrows(DomainException.class, () -> new Email("no-es-email"));
    }
}
