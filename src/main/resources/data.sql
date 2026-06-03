insert into usuarios (id, nombre, correo_electronico, telefono, contrasena_hash, activo, rol, fecha_registro, ultimo_acceso)
values
    (1, 'Administrador', 'admin@helpdesk.com', '+573001110001', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'ADMINISTRADOR', current_timestamp, null),
    (2, 'Tecnico Uno', 'tecnico1@helpdesk.com', '+573001110002', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'TECNICO', current_timestamp, null),
    (3, 'Tecnico Dos', 'tecnico2@helpdesk.com', '+573001110003', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'TECNICO', current_timestamp, null),
    (4, 'Cliente Uno', 'cliente1@helpdesk.com', '+573001110004', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'CLIENTE', current_timestamp, null),
    (5, 'Cliente Dos', 'cliente2@helpdesk.com', '+573001110005', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'CLIENTE', current_timestamp, null)
on conflict (id) do nothing;

insert into acuerdos_servicio (id, nombre, descripcion, nivel_prioridad, tiempo_max_respuesta_horas, tiempo_max_resolucion_horas, activo, fecha_creacion)
values
    (1, 'SLA Baja', 'Atencion para incidencias de baja prioridad', 'BAJA', 24, 120, true, current_timestamp),
    (2, 'SLA Media', 'Atencion para incidencias de prioridad media', 'MEDIA', 8, 48, true, current_timestamp),
    (3, 'SLA Alta', 'Atencion para incidencias de alta prioridad', 'ALTA', 2, 8, true, current_timestamp),
    (4, 'SLA Critica', 'Atencion para incidencias criticas', 'CRITICA', 1, 4, true, current_timestamp)
on conflict (id) do nothing;
