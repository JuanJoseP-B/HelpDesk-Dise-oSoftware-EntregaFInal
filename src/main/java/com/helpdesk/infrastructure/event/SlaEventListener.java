package com.helpdesk.infrastructure.event;

import com.helpdesk.application.service.NotificacionApplicationService;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.event.SlaVioladoEvent;
import com.helpdesk.domain.repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Listener de violaciones SLA para alertar administradores.
 */
@Component
public class SlaEventListener {

    private final UsuarioRepository usuarioRepository;
    private final NotificacionApplicationService notificacionApplicationService;

    public SlaEventListener(
            UsuarioRepository usuarioRepository,
            NotificacionApplicationService notificacionApplicationService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.notificacionApplicationService = notificacionApplicationService;
    }

    @Async("applicationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSlaViolado(SlaVioladoEvent event) {
        usuarioRepository.findByRol(RolUsuario.ADMINISTRADOR).stream()
                .filter(usuario -> usuario.isActivo())
                .forEach(admin -> notificacionApplicationService.onSlaViolado(event, admin.getId()));
    }
}
