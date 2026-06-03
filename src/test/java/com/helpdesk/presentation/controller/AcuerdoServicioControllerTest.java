package com.helpdesk.presentation.controller;

import com.helpdesk.application.usecase.AcuerdoServicioUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AcuerdoServicioController.class)
@AutoConfigureMockMvc(addFilters = false)
class AcuerdoServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AcuerdoServicioUseCase acuerdoServicioUseCase;

    @Test
    void listarRetornaOk() throws Exception {
        when(acuerdoServicioUseCase.listarActivos()).thenReturn(List.of());

        mockMvc.perform(get("/slas"))
                .andExpect(status().isOk());
    }
}
