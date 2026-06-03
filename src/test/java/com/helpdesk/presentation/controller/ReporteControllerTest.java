package com.helpdesk.presentation.controller;

import com.helpdesk.application.dto.response.DashboardMetricsDTO;
import com.helpdesk.application.usecase.ReporteUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReporteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteUseCase reporteUseCase;

    @Test
    void dashboardRetornaOk() throws Exception {
        when(reporteUseCase.obtenerDashboard()).thenReturn(new DashboardMetricsDTO(1, 1, 0, 0, 0, 0));

        mockMvc.perform(get("/reportes/dashboard"))
                .andExpect(status().isOk());
    }
}
