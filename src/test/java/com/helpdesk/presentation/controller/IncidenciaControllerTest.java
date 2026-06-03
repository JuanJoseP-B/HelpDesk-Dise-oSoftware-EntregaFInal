package com.helpdesk.presentation.controller;

import com.helpdesk.application.dto.response.IncidenciaDetalleDTO;
import com.helpdesk.application.usecase.ComentarioUseCase;
import com.helpdesk.application.usecase.IncidenciaUseCase;
import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidenciaController.class)
@AutoConfigureMockMvc(addFilters = false)
class IncidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidenciaUseCase incidenciaUseCase;

    @MockBean
    private ComentarioUseCase comentarioUseCase;

    @Test
    void detalleRetornaOk() throws Exception {
        when(incidenciaUseCase.obtenerDetalle(1L)).thenReturn(Optional.of(new IncidenciaDetalleDTO(
                1L,
                "Ticket",
                EstadoIncidencia.ABIERTO,
                NivelPrioridad.MEDIA,
                LocalDateTime.now(),
                "Descripcion",
                null,
                10L,
                null,
                false,
                List.of(),
                List.of()
        )));

        mockMvc.perform(get("/incidencias/1"))
                .andExpect(status().isOk());
    }
}
