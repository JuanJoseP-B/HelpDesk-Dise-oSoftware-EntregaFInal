package com.helpdesk.presentation.controller;

import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.port.GeneradorContrasena;
import com.helpdesk.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private GeneradorContrasena generadorContrasena;

    @BeforeEach
    void prepararUsuario() {
        if (!usuarioRepository.existsByCorreoElectronico("login-it@test.com")) {
            usuarioRepository.save(new Usuario(
                    null,
                    "Login IT",
                    "login-it@test.com",
                    "+573001112233",
                    generadorContrasena.generarHash("admin123"),
                    true,
                    RolUsuario.ADMINISTRADOR,
                    LocalDateTime.now(),
                    null
            ));
        }
    }

    @Test
    void loginRetornaJwtReal() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"email":"login-it@test.com","password":"admin123"}
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    void registerCreaCliente() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Cliente Registro IT","email":"registro-it@test.com","password":"cliente123","telefono":"+573009990000"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("registro-it@test.com"))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }
}
