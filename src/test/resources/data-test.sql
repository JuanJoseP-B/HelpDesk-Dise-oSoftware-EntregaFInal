delete from rol_permisos;
delete from historial_estados;
delete from comentarios;
delete from asignaciones;
delete from notificaciones;
delete from incidencias;
delete from acuerdos_servicio;
delete from usuarios;
delete from roles;

insert into usuarios (id, nombre, correo_electronico, telefono, contrasena_hash, activo, rol, fecha_registro, ultimo_acceso)
values
    (1, 'Administrador Test', 'admin@test.com', '+573001110001', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'ADMINISTRADOR', current_timestamp, null),
    (2, 'Tecnico Test', 'tecnico@test.com', '+573001110002', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'TECNICO', current_timestamp, null),
    (3, 'Cliente Test', 'cliente@test.com', '+573001110003', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHIqz4ON9VfSc95QSOqq7uOWi7Mkxm5a', true, 'CLIENTE', current_timestamp, null);

insert into acuerdos_servicio (id, nombre, descripcion, nivel_prioridad, tiempo_max_respuesta_horas, tiempo_max_resolucion_horas, activo, fecha_creacion)
values
    (1, 'SLA Test Baja', 'Datos de prueba para prioridad baja', 'BAJA', 24, 120, true, current_timestamp),
    (2, 'SLA Test Media', 'Datos de prueba para prioridad media', 'MEDIA', 8, 48, true, current_timestamp),
    (3, 'SLA Test Alta', 'Datos de prueba para prioridad alta', 'ALTA', 2, 8, true, current_timestamp),
    (4, 'SLA Test Critica', 'Datos de prueba para prioridad critica', 'CRITICA', 1, 4, true, current_timestamp);

insert into incidencias (id, titulo, descripcion, fecha_creacion, fecha_asignacion, fecha_resolucion, fecha_cierre, estado, nivel_prioridad, solucion, tecnico_asignado_id, cliente_id, acuerdo_servicio_id, sla_violado)
values
    (1, 'Incidencia abierta test', 'Incidencia de prueba para consultas', current_timestamp, null, null, null, 'ABIERTO', 'MEDIA', null, null, 3, 2, false),
    (2, 'Incidencia asignada test', 'Incidencia de prueba asignada', current_timestamp, current_timestamp, null, null, 'ASIGNADO', 'ALTA', null, 2, 3, 3, false);
