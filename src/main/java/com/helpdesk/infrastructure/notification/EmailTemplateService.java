package com.helpdesk.infrastructure.notification;

import com.helpdesk.domain.entity.Notificacion;
import org.springframework.stereotype.Service;

/**
 * Construye plantillas simples de email para notificaciones del HelpDesk.
 */
@Service
public class EmailTemplateService {

    public String generarTexto(Notificacion notificacion) {
        return """
                %s

                %s

                Equipo HelpDesk
                """.formatted(notificacion.getAsunto(), notificacion.getMensaje());
    }

    public String generarHtml(Notificacion notificacion) {
        return """
                <html>
                  <body style="font-family:Arial,sans-serif;color:#1f2937">
                    <h2>%s</h2>
                    <p>%s</p>
                    <hr>
                    <p style="font-size:12px;color:#6b7280">Equipo HelpDesk</p>
                  </body>
                </html>
                """.formatted(escapar(notificacion.getAsunto()), escapar(notificacion.getMensaje()));
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
