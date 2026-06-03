package com.helpdesk.application.config;

import com.helpdesk.domain.port.DomainEventPublisher;
import com.helpdesk.domain.repository.AsignacionRepository;
import com.helpdesk.domain.repository.UsuarioRepository;
import com.helpdesk.domain.service.AsignacionService;
import com.helpdesk.domain.service.SlaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuración de la capa de aplicación y beans de servicios de dominio.
 */
@Configuration
@ComponentScan(basePackages = "com.helpdesk.application")
@EnableTransactionManagement
public class ApplicationConfig {

    @Bean
    public AsignacionService asignacionService(
            UsuarioRepository usuarioRepository,
            AsignacionRepository asignacionRepository
    ) {
        return new AsignacionService(usuarioRepository, asignacionRepository);
    }

    @Bean
    public SlaService slaService() {
        return new SlaService();
    }

    /**
     * Publicador de eventos por defecto (no-op) hasta que infraestructura provea uno real.
     */
    @Bean
    @ConditionalOnMissingBean(DomainEventPublisher.class)
    public DomainEventPublisher domainEventPublisher() {
        return evento -> { };
    }
}
