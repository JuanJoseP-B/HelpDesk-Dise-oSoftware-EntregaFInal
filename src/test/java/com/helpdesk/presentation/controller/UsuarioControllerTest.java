package com.helpdesk.presentation.controller;

import com.helpdesk.application.dto.response.UsuarioResponseDTO;
import com.helpdesk.application.usecase.UsuarioUseCase;
import com.helpdesk.domain.enums.RolUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioUseCase usuarioUseCase;

    @Test
    void obtenerRetornaOk() throws Exception {
        when(usuarioUseCase.obtenerPorId(1L)).thenReturn(Optional.of(new UsuarioResponseDTO(
                1L,
                "Admin",
                "admin@test.com",
                null,
                RolUsuario.ADMINISTRADOR,
                true,
                LocalDateTime.now()
        )));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk());
    }
}
