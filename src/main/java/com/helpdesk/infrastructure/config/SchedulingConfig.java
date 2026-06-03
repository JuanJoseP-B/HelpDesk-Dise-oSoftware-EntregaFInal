package com.helpdesk.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Habilita tareas programadas de infraestructura.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
}
