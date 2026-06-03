package com.helpdesk.presentation.controller;

import com.helpdesk.application.dto.response.ComentarioDTO;
import com.helpdesk.application.usecase.ComentarioUseCase;
import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.infrastructure.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComentarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentarioUseCase comentarioUseCase;

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void editarRetornaOk() throws Exception {
        autenticar();
        when(comentarioUseCase.editar(eq(1L), any(), eq(7L)))
                .thenReturn(new ComentarioDTO(1L, "Actualizado", 7L, LocalDateTime.now(), true));

        mockMvc.perform(put("/comentarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"contenido":"Actualizado"}
                                """))
                .andExpect(status().isOk());
    }

    private void autenticar() {
        Usuario usuario = new Usuario(7L, "Tecnico", "tecnico@test.com", null, "hash", true, RolUsuario.TECNICO, LocalDateTime.now(), null);
        CustomUserDetails userDetails = new CustomUserDetails(usuario);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        ));
    }
}
