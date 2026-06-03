package com.helpdesk.infrastructure.notification;

import com.helpdesk.domain.entity.Notificacion;
import com.helpdesk.domain.port.NotificationPort;
import com.helpdesk.domain.repository.UsuarioRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Adapter SMTP para enviar notificaciones por email.
 */
@Component
public class SmtpNotificationAdapter implements NotificationPort {

    private final JavaMailSender mailSender;
    private final UsuarioRepository usuarioRepository;
    private final EmailTemplateService templateService;

    public SmtpNotificationAdapter(
            JavaMailSender mailSender,
            UsuarioRepository usuarioRepository,
            EmailTemplateService templateService
    ) {
        this.mailSender = mailSender;
        this.usuarioRepository = usuarioRepository;
        this.templateService = templateService;
    }

    @Override
    public void enviar(Notificacion notificacion) {
        String destinatario = usuarioRepository.findById(notificacion.getDestinatarioId())
                .orElseThrow(() -> new IllegalArgumentException("Destinatario no encontrado: " + notificacion.getDestinatarioId()))
                .getCorreoElectronico();
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject(notificacion.getAsunto());
            helper.setText(templateService.generarTexto(notificacion), templateService.generarHtml(notificacion));
            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new IllegalStateException("No fue posible construir el email", ex);
        }
    }
}
