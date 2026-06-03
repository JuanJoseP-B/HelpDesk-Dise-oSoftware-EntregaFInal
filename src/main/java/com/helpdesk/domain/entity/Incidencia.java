package com.helpdesk.domain.entity;

import com.helpdesk.domain.enums.EstadoIncidencia;
import com.helpdesk.domain.enums.NivelPrioridad;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.exception.DomainException;
import com.helpdesk.domain.exception.TransicionEstadoInvalidaException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Agregado raíz que representa una incidencia del HelpDesk con su ciclo de vida.
 */
public class Incidencia {

    private static final int DIAS_MAX_REAPERTURA = 30;

    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaResolucion;
    private LocalDateTime fechaCierre;
    private EstadoIncidencia estado;
    private NivelPrioridad nivelPrioridad;
    private String solucion;
    private Long tecnicoAsignadoId;
    private Long clienteId;
    private Long acuerdoServicioId;
    private boolean slaViolado;
    private final List<Comentario> comentarios;
    private final List<HistorialEstado> historial;

    public Incidencia() {
        this.estado = EstadoIncidencia.ABIERTO;
        this.comentarios = new ArrayList<>();
        this.historial = new ArrayList<>();
    }

    public Incidencia(
            Long id,
            String titulo,
            String descripcion,
            LocalDateTime fechaCreacion,
            NivelPrioridad nivelPrioridad,
            Long clienteId,
            Long acuerdoServicioId
    ) {
        this();
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion != null ? fechaCreacion : LocalDateTime.now();
        this.nivelPrioridad = nivelPrioridad;
        this.clienteId = clienteId;
        this.acuerdoServicioId = acuerdoServicioId;
    }

    /**
     * Reconstituye una incidencia desde persistencia sin ejecutar transiciones de negocio.
     */
    public static Incidencia reconstituir(
            Long id,
            String titulo,
            String descripcion,
            LocalDateTime fechaCreacion,
            LocalDateTime fechaAsignacion,
            LocalDateTime fechaResolucion,
            LocalDateTime fechaCierre,
            EstadoIncidencia estado,
            NivelPrioridad nivelPrioridad,
            String solucion,
            Long tecnicoAsignadoId,
            Long clienteId,
            Long acuerdoServicioId,
            boolean slaViolado
    ) {
        Incidencia incidencia = new Incidencia(id, titulo, descripcion, fechaCreacion, nivelPrioridad, clienteId, acuerdoServicioId);
        incidencia.fechaAsignacion = fechaAsignacion;
        incidencia.fechaResolucion = fechaResolucion;
        incidencia.fechaCierre = fechaCierre;
        incidencia.estado = estado;
        incidencia.solucion = solucion;
        incidencia.tecnicoAsignadoId = tecnicoAsignadoId;
        incidencia.slaViolado = slaViolado;
        return incidencia;
    }

    /**
     * Asigna la incidencia a un técnico. Permite re-asignación desde ASIGNADO o EN_PROGRESO.
     */
    public void asignar(Long tecnicoId, LocalDateTime fecha, RolUsuario rolEjecutor) {
        validarRol(rolEjecutor, EnumSet.of(RolUsuario.ADMINISTRADOR, RolUsuario.TECNICO));
        if (tecnicoId == null) {
            throw new DomainException("El técnico asignado es obligatorio");
        }
        if (fecha == null) {
            throw new DomainException("La fecha de asignación es obligatoria");
        }

        if (estado == EstadoIncidencia.ABIERTO) {
            cambiarEstado(EstadoIncidencia.ASIGNADO, rolEjecutor, "Asignación inicial");
        } else if (estado == EstadoIncidencia.ASIGNADO || estado == EstadoIncidencia.EN_PROGRESO) {
            registrarHistorialInterno(estado, estado, null, "Re-asignación de técnico");
        } else {
            throw new TransicionEstadoInvalidaException(
                    "No se puede asignar una incidencia en estado " + estado
            );
        }

        this.tecnicoAsignadoId = tecnicoId;
        this.fechaAsignacion = fecha;
    }

    /**
     * Inicia el trabajo sobre la incidencia (ASIGNADO → EN_PROGRESO).
     */
    public void iniciarTrabajo(RolUsuario rolEjecutor) {
        validarRol(rolEjecutor, EnumSet.of(RolUsuario.TECNICO, RolUsuario.ADMINISTRADOR));
        cambiarEstado(EstadoIncidencia.EN_PROGRESO, rolEjecutor, "Trabajo iniciado");
    }

    /**
     * Resuelve la incidencia con la solución indicada (EN_PROGRESO → RESUELTO).
     */
    public void resolver(String solucionTexto, LocalDateTime fecha, RolUsuario rolEjecutor) {
        validarRol(rolEjecutor, EnumSet.of(RolUsuario.TECNICO, RolUsuario.ADMINISTRADOR));
        if (solucionTexto == null || solucionTexto.isBlank()) {
            throw new DomainException("La solución es obligatoria al resolver");
        }
        if (fecha == null) {
            throw new DomainException("La fecha de resolución es obligatoria");
        }
        cambiarEstado(EstadoIncidencia.RESUELTO, rolEjecutor, "Incidencia resuelta");
        this.solucion = solucionTexto.trim();
        this.fechaResolucion = fecha;
    }

    /**
     * Cierra la incidencia (RESUELTO → CERRADO).
     */
    public void cerrar(LocalDateTime fecha, RolUsuario rolEjecutor) {
        validarRol(rolEjecutor, EnumSet.of(RolUsuario.CLIENTE, RolUsuario.ADMINISTRADOR));
        if (fecha == null) {
            throw new DomainException("La fecha de cierre es obligatoria");
        }
        cambiarEstado(EstadoIncidencia.CERRADO, rolEjecutor, "Incidencia cerrada");
        this.fechaCierre = fecha;
    }

    /**
     * Reabre una incidencia cerrada (CERRADO → ABIERTO). Solo cliente, dentro de 30 días.
     */
    public void reabrir(String motivo, RolUsuario rolEjecutor) {
        if (rolEjecutor != RolUsuario.CLIENTE && rolEjecutor != RolUsuario.ADMINISTRADOR) {
            throw new TransicionEstadoInvalidaException("Solo el cliente o administrador puede reabrir");
        }
        if (estado != EstadoIncidencia.CERRADO) {
            throw new TransicionEstadoInvalidaException(estado, EstadoIncidencia.ABIERTO);
        }
        if (fechaCierre != null && rolEjecutor == RolUsuario.CLIENTE) {
            long dias = ChronoUnit.DAYS.between(fechaCierre, LocalDateTime.now());
            if (dias > DIAS_MAX_REAPERTURA) {
                throw new TransicionEstadoInvalidaException(
                        "No se puede reabrir: han transcurrido más de " + DIAS_MAX_REAPERTURA + " días"
                );
            }
        }
        cambiarEstado(EstadoIncidencia.ABIERTO, rolEjecutor, motivo != null ? motivo : "Reapertura");
        this.fechaCierre = null;
        this.fechaResolucion = null;
        this.solucion = null;
        this.tecnicoAsignadoId = null;
        this.fechaAsignacion = null;
        this.slaViolado = false;
    }

    /**
     * Escala la prioridad de la incidencia (solo administrador).
     */
    public void escalarPrioridad(NivelPrioridad nueva, RolUsuario rolEjecutor) {
        validarRol(rolEjecutor, EnumSet.of(RolUsuario.ADMINISTRADOR));
        if (nueva == null) {
            throw new DomainException("La nueva prioridad es obligatoria");
        }
        if (estado == EstadoIncidencia.CERRADO) {
            throw new DomainException("No se puede cambiar la prioridad de una incidencia cerrada");
        }
        this.nivelPrioridad = nueva;
    }

    /**
     * Indica si la incidencia ha superado el SLA según el acuerdo de servicio.
     */
    public boolean estaVencida(LocalDateTime ahora, AcuerdoServicio sla) {
        if (ahora == null || sla == null || !sla.estaActivo() || fechaCreacion == null) {
            return false;
        }
        if (estado == EstadoIncidencia.CERRADO || estado == EstadoIncidencia.RESUELTO) {
            return false;
        }
        LocalDateTime limiteRespuesta = sla.calcularFechaLimiteRespuesta(fechaCreacion);
        if (estado == EstadoIncidencia.ABIERTO && ahora.isAfter(limiteRespuesta)) {
            return true;
        }
        LocalDateTime limiteResolucion = sla.calcularFechaLimiteResolucion(fechaCreacion);
        return ahora.isAfter(limiteResolucion);
    }

    /**
     * Marca la incidencia como con SLA violado.
     */
    public void marcarSlaViolado() {
        this.slaViolado = true;
    }

    /**
     * Agrega un comentario a la incidencia.
     */
    public void agregarComentario(Comentario comentario) {
        if (comentario == null) {
            throw new DomainException("El comentario no puede ser nulo");
        }
        comentarios.add(comentario);
    }

    /**
     * Registra un cambio de estado en el historial interno.
     */
    public void registrarHistorial(HistorialEstado registro) {
        if (registro == null) {
            throw new DomainException("El registro de historial no puede ser nulo");
        }
        historial.add(registro);
    }

    private void cambiarEstado(EstadoIncidencia nuevoEstado, RolUsuario rolEjecutor, String motivo) {
        if (!esTransicionPermitida(estado, nuevoEstado, rolEjecutor)) {
            throw new TransicionEstadoInvalidaException(estado, nuevoEstado);
        }
        EstadoIncidencia anterior = this.estado;
        this.estado = nuevoEstado;
        registrarHistorialInterno(anterior, nuevoEstado, null, motivo);
    }

    private void registrarHistorialInterno(
            EstadoIncidencia anterior,
            EstadoIncidencia nuevo,
            Long usuarioId,
            String motivo
    ) {
        historial.add(HistorialEstado.crear(
                this.id,
                anterior,
                nuevo,
                usuarioId,
                motivo
        ));
    }

    private boolean esTransicionPermitida(
            EstadoIncidencia origen,
            EstadoIncidencia destino,
            RolUsuario rolEjecutor
    ) {
        if (origen == destino) {
            return true;
        }
        if (rolEjecutor == RolUsuario.ADMINISTRADOR && rolEjecutor.tienePermiso("SALTAR_ESTADOS")) {
            return esTransicionAdminValida(origen, destino);
        }
        return transicionesNormales.get(origen).contains(destino);
    }

    private static final Map<EstadoIncidencia, Set<EstadoIncidencia>> transicionesNormales =
            Map.of(
                    EstadoIncidencia.ABIERTO, Set.of(EstadoIncidencia.ASIGNADO),
                    EstadoIncidencia.ASIGNADO, Set.of(EstadoIncidencia.EN_PROGRESO),
                    EstadoIncidencia.EN_PROGRESO, Set.of(EstadoIncidencia.RESUELTO),
                    EstadoIncidencia.RESUELTO, Set.of(EstadoIncidencia.CERRADO),
                    EstadoIncidencia.CERRADO, Set.of(EstadoIncidencia.ABIERTO)
            );

    private boolean esTransicionAdminValida(EstadoIncidencia origen, EstadoIncidencia destino) {
        int ordenOrigen = origen.ordinal();
        int ordenDestino = destino.ordinal();
        if (origen == EstadoIncidencia.CERRADO && destino == EstadoIncidencia.ABIERTO) {
            return true;
        }
        return ordenDestino > ordenOrigen || ordenDestino == EstadoIncidencia.CERRADO.ordinal();
    }

    private void validarRol(RolUsuario rolEjecutor, Set<RolUsuario> rolesPermitidos) {
        if (rolEjecutor == null || !rolesPermitidos.contains(rolEjecutor)) {
            throw new TransicionEstadoInvalidaException("Rol no autorizado para esta operación");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public EstadoIncidencia getEstado() {
        return estado;
    }

    public NivelPrioridad getNivelPrioridad() {
        return nivelPrioridad;
    }

    public String getSolucion() {
        return solucion;
    }

    public Long getTecnicoAsignadoId() {
        return tecnicoAsignadoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getAcuerdoServicioId() {
        return acuerdoServicioId;
    }

    public boolean isSlaViolado() {
        return slaViolado;
    }

    public List<Comentario> getComentarios() {
        return Collections.unmodifiableList(comentarios);
    }

    public List<HistorialEstado> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Incidencia that = (Incidencia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
