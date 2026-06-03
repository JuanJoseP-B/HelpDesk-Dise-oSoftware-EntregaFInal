package com.helpdesk.presentation.controller;

import com.helpdesk.application.dto.response.JwtResponseDTO;
import com.helpdesk.application.usecase.AutenticacionUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutenticacionUseCase autenticacionUseCase;

    @Test
    void loginRetornaOk() throws Exception {
        when(autenticacionUseCase.login(any())).thenReturn(new JwtResponseDTO("token", "refresh", "Bearer", 3600));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"admin@helpdesk.com","password":"admin123"}
                                """))
                .andExpect(status().isOk());
    }
}
