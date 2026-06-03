# 🧠 PROMPT MASTER - HELPDESK API (Spring Boot + Arquitectura Hexagonal)

> **Versión:** 1.0.0  
> **IDE Target:** Cursor (AI Code Editor)  
> **Stack:** Java 17, Spring Boot 3.2, PostgreSQL, JPA/Hibernate, JWT, SMTP  
> **Patrón:** Arquitectura Hexagonal (Ports & Adapters) + CQRS  
> **Workflow:** Git Flow (feature branches → develop → main)

---

## 📋 ÍNDICE

1. [Contexto del Proyecto](#1-contexto-del-proyecto)
2. [Reglas Estrictas de Generación](#2-reglas-estrictas-de-generación-rules)
3. [Estrategia Git Flow](#3-estrategia-git-flow)
4. [Fases de Desarrollo](#4-fases-de-desarrollo-phases)
5. [Prompts por Fase](#5-prompts-por-fase)
6. [Comandos Git Rápidos](#6-comandos-git-rápidos)
7. [Checklist de Validación](#7-checklist-de-validación)

---

## 1. CONTEXTO DEL PROYECTO (CONTEXT)

### 1.1 Descripción
Sistema **HelpDesk/Service Desk** de gestión de incidencias. **SOLO BACKEND API REST** (sin frontend). Consumido por navegadores web, apps móviles o Postman.

### 1.2 Actores
| Actor | Rol | Acciones |
|-------|-----|----------|
| **Cliente** | Usuario final | Crear incidencias, consultar estado, recibir notificaciones |
| **Técnico** | Soporte | Ver asignadas, actualizar estado, comentar, resolver |
| **Administrador** | Gestor | CRUD usuarios, asignar tickets, ver reportes, configurar SLAs |

### 1.3 Flujo de Estados
```
ABIERTO → ASIGNADO → EN_PROGRESO → RESUELTO → CERRADO
```
- Re-apertura permitida desde CERRADO → ABIERTO (solo Cliente, < 30 días)
- Re-asignación permitida desde ASIGNADO/EN_PROGRESO

### 1.4 Prioridades y SLAs
| Prioridad | Respuesta | Resolución |
|-----------|-----------|------------|
| BAJA | 24h | 5 días |
| MEDIA | 8h | 2 días |
| ALTA | 2h | 8h |
| CRÍTICA | 30min | 4h |

- Cálculo en horas hábiles (L-V, 8am-6pm)
- Violación genera `SlaVioladoEvent` → notificación a Admin

### 1.5 Tecnologías
- Java 17
- Spring Boot 3.2.0
- Spring Data JPA (Hibernate 6)
- Spring Security + JWT (jjwt 0.12.3)
- Spring Mail (SMTP)
- PostgreSQL 15
- Lombok 1.18.30
- MapStruct 1.5.5.Final
- JUnit 5 + Mockito
- Maven

### 1.6 Arquitectura Hexagonal (4 Capas)

```
┌─────────────────────────────────────────┐
│         PRESENTACIÓN (Adapters IN)      │
│    Controllers REST, JWT Filter,        │
│    Exception Handler, Request DTOs      │
├─────────────────────────────────────────┤
│         APLICACIÓN (Use Cases)          │
│    Commands, Queries, DTOs, App Services│
│    Orquestación, Transacciones          │
├─────────────────────────────────────────┤
│         DOMINIO (Core Business)         │
│    Entities, Value Objects, Events,     │
│    Repository Interfaces (Ports),         │
│    Domain Services, Enums               │
├─────────────────────────────────────────┤
│      INFRAESTRUCTURA (Adapters OUT)     │
│    JPA Repositories, SMTP Adapter,      │
│    Event Publisher, JWT Config,         │
│    Security, Mappers, DB Entities       │
└─────────────────────────────────────────┘
```

### 1.7 Modelo de Dominio (Resumen)

**Entidades:**
- `Incidencia` (id, titulo, descripcion, estados, fechas, prioridad, slaViolado)
- `Usuario` (id, nombre, email, telefono, passwordHash, activo, rol)
- `Rol` (id, tipo, permisos)
- `Asignacion` (id, incidenciaId, tecnicoId, asignadoPorId, fecha, activa)
- `AcuerdoServicio` (id, nombre, prioridad, tiempos, activo)
- `Notificacion` (id, destinatarioId, tipo, asunto, mensaje, estadoEnvio)
- `HistorialEstado` (id, incidenciaId, estadoAnterior, estadoNuevo, fecha, usuarioId, motivo)
- `Comentario` (id, incidenciaId, autorId, contenido, fecha, visibleParaCliente)

**Enums:**
- `EstadoIncidencia`: ABIERTO, ASIGNADO, EN_PROGRESO, RESUELTO, CERRADO
- `NivelPrioridad`: BAJA(24,120), MEDIA(8,48), ALTA(2,8), CRITICA(1,4)
- `RolUsuario`: CLIENTE, TECNICO, ADMINISTRADOR (con permisos)
- `TipoNotificacion`: CREACION_TICKET, ASIGNACION, CAMBIO_ESTADO, RESOLUCION, SLA_VIOLADO, COMENTARIO_NUEVO

**Eventos:**
- `TicketCreadoEvent`
- `EstadoCambiadoEvent`
- `TicketAsignadoEvent`
- `TicketResueltoEvent`
- `SlaVioladoEvent`

### 1.8 API REST Endpoints (Resumen)

**Auth:**
- `POST /auth/login`, `POST /auth/register`, `POST /auth/refresh`

**Incidencias (Comandos):**
- `POST /incidencias` (crear)
- `PUT /incidencias/{id}/asignar`
- `PUT /incidencias/{id}/iniciar`
- `PUT /incidencias/{id}/resolver`
- `PUT /incidencias/{id}/cerrar`
- `PUT /incidencias/{id}/reabrir`
- `PUT /incidencias/{id}/prioridad`

**Incidencias (Consultas):**
- `GET /incidencias` (admin)
- `GET /incidencias/mis-tickets` (cliente)
- `GET /incidencias/asignadas` (tecnico)
- `GET /incidencias/{id}`
- `GET /incidencias/{id}/historial`
- `GET /incidencias/{id}/comentarios`

**Comentarios:**
- `POST /incidencias/{id}/comentarios`
- `PUT /comentarios/{id}`
- `DELETE /comentarios/{id}`

**Usuarios (Admin):**
- `GET /usuarios`, `GET /usuarios/{id}`, `POST /usuarios`, `PUT /usuarios/{id}`, `PUT /usuarios/{id}/rol`, `DELETE /usuarios/{id}`

**SLAs (Admin):**
- `GET /slas`, `GET /slas/{id}`, `POST /slas`, `PUT /slas/{id}`, `DELETE /slas/{id}`

**Reportes (Admin):**
- `GET /reportes/dashboard`
- `GET /reportes/sla-cumplimiento`
- `GET /reportes/tecnico-rendimiento`
- `GET /reportes/incidencias-por-estado`

### 1.9 Configuración Clave
- JWT: secreto desde env, expiración 24h, refresh 7 días
- DB: PostgreSQL, HikariCP, ddl-auto=validate
- Mail: SMTP (Gmail/SendGrid/AWS SES compatible)
- Server: puerto 8080, context-path `/api/v1`
- JPA: batch_size 20, open-in-view=false

---

## 2. REGLAS ESTRICTAS DE GENERACIÓN (RULES)

### 🔴 REGLAS ABSOLUTAS (Nunca romper)

| # | Regla | Severidad |
|---|-------|-----------|
| R1 | **Java 17** obligatorio. Usar `var`, `Optional`, Streams, Lambdas, Records donde aplique | 🔴 |
| R2 | **Spring Boot 3.2.0**. Usar Jakarta EE (no javax). Validación con `jakarta.validation` | 🔴 |
| R3 | **Arquitectura Hexagonal Estricta**. Capa Dominio NUNCA importa `org.springframework.*`, `javax.persistence.*`, `lombok.*` | 🔴 |
| R4 | **Inyección por Constructor** obligatoria. **NUNCA** usar `@Autowired` en campos | 🔴 |
| R5 | **NUNCA** mezclar capas. Dominio no conoce JPA, SMTP, HTTP, JSON | 🔴 |
| R6 | Repositorios de Dominio son **interfaces puras** (puertos). Implementación JPA va en `infrastructure.persistence` | 🔴 |
| R7 | Entidades JPA (infraestructura) NUNCA se exponen en controllers. Usar DTOs + Mappers | 🔴 |
| R8 | **Optional<>** obligatorio para retornos que pueden ser null desde repositorios y servicios | 🔴 |
| R9 | **ResponseEntity<>** obligatorio en todos los controllers con códigos HTTP correctos (201, 200, 404, 409, etc.) | 🔴 |
| R10 | **Javadoc** en todas las clases públicas y métodos de servicio | 🔴 |
| R11 | **Lombok** permitido SOLO en infraestructura y presentación. En dominio preferir constructores/explicito (o usar con moderación) | 🟡 |
| R12 | **Jakarta Validation** (`@NotNull`, `@NotBlank`, `@Size`, `@Email`) en todos los Request DTOs | 🔴 |
| R13 | **@ControllerAdvice** global para manejo de excepciones. Excepciones custom: `BusinessException`, `ResourceNotFoundException`, `TransicionEstadoInvalidaException` | 🔴 |
| R14 | **Tests Unitarios** con JUnit 5 + Mockito para toda lógica de dominio y aplicación | 🔴 |
| R15 | **Git Flow**: Cada fase es una feature branch. Commits atómicos con mensajes convencionales (`feat:`, `test:`, `refactor:`) | 🔴 |

### 🟡 REGLAS DE ESTILO

| # | Regla |
|---|-------|
| S1 | Nombres en español para dominio (negocio), inglés para infraestructura técnica si es estándar (ej: `UserDetails`) |
| S2 | Paquete base: `com.helpdesk` |
| S3 | Métodos de dominio en español: `asignar()`, `resolver()`, `cerrar()`, `reabrir()` |
| S4 | Endpoints en español: `/incidencias`, `/usuarios`, `/slas` |
| S5 | DTOs terminan en `DTO`, Commands en `Command`, Events en `Event`, Repositories en `Repository` |
| S6 | Usar `Pageable` y `Page<>` para endpoints de listado |
| S7 | Fechas con `java.time.LocalDateTime` (nunca `Date` o `Calendar`) |
| S8 | Passwords hasheados con `BCryptPasswordEncoder` |

### 🟢 REGLAS DE CALIDAD

| # | Regla |
|---|-------|
| Q1 | Principio **Tell, Don't Ask** en entidades de dominio |
| Q2 | **Inmutabilidad** preferida para Value Objects y Events |
| Q3 | **CQRS**: Separar claramente Commands (escritura) de Queries (lectura) |
| Q4 | **Event-Driven**: Eventos de dominio publicados dentro de transacciones, listeners asíncronos |
| Q5 | **Idempotencia** en operaciones críticas (asignación, cambio estado) |

---

## 3. ESTRATEGIA GIT FLOW

### 3.1 Ramas Principales

```
main        ← producción estable (solo releases)
develop     ← integración continua (features mergeadas)
  │
  ├── feature/F1-dominio-entidades
  ├── feature/F2-dominio-eventos-repos
  ├── feature/F3-aplicacion-dtos-commands
  ├── feature/F4-aplicacion-usecases
  ├── feature/F5-infraestructura-jpa
  ├── feature/F6-infraestructura-security
  ├── feature/F7-infraestructura-mail
  ├── feature/F8-presentacion-controllers
  ├── feature/F9-presentacion-exceptions
  ├── feature/F10-configuracion-tests
  └── feature/F11-integracion-final
```

### 3.2 Convención de Commits
```
feat: agregar entidad Incidencia con estados y transiciones
feat: implementar repositorio JPA para Usuario
test: agregar tests unitarios para cambio de estado
refactor: extraer validación de SLA a servicio de dominio
fix: corregir transición inválida ABIERTO→CERRADO
docs: actualizar Javadoc de IncidenciaService
```

### 3.3 Flujo de Trabajo por Fase

```bash
# Iniciar fase
git checkout develop
git pull origin develop
git checkout -b feature/FX-nombre-fase

# Desarrollar con Cursor (pegar prompts de esta guía)
# ... generar código ...

# Validar antes de commit
mvn clean test
mvn clean verify

# Commit atómico
git add .
git commit -m "feat: descripción del cambio"

# Finalizar fase
git checkout develop
git merge --no-ff feature/FX-nombre-fase
git push origin develop

# Opcional: borrar feature branch
git branch -d feature/FX-nombre-fase
```

---

## 4. FASES DE DESARROLLO (PHASES)

| Fase | Nombre | Rama | Focus |
|------|--------|------|-------|
| **F1** | Dominio Core | `feature/F1-dominio-core` | Entidades, Enums, VO, Eventos |
| **F2** | Dominio Ports | `feature/F2-dominio-ports` | Interfaces de Repositorio, Domain Services |
| **F3** | Aplicación | `feature/F3-aplicacion` | DTOs, Commands, Use Cases, App Services |
| **F4** | Infraestructura Persistencia | `feature/F4-infra-jpa` | JPA Entities, Repositories, Mappers |
| **F5** | Infraestructura Adapters | `feature/F5-infra-adapters` | Security JWT, SMTP Mail, Event Publisher |
| **F6** | Presentación | `feature/F6-presentacion` | Controllers, Filters, Exception Handler |
| **F7** | Configuración y Tests | `feature/F7-config-tests` | application.yml, Tests integrales, data.sql |
| **F8** | Integración y QA | `feature/F8-integracion` | Ajustes finales, Postman collection, README |

---

## 5. PROMPTS POR FASE

> **Instrucciones de uso:** Copia el bloque completo de la fase que vas a ejecutar, pégalo en el chat de Cursor (Ctrl+K o Cmd+K), y presiona Enter. Cursor generará el código. Revisa y ajusta antes de commitear.

---

### 🟦 FASE 1: DOMINIO CORE (Entidades, Enums, Value Objects, Eventos)
**Rama:** `feature/F1-dominio-core`

```
CONTEXT:
Estoy desarrollando un sistema HelpDesk backend API REST con Spring Boot 3.2 y Java 17. 
Esta es la FASE 1: Capa de Dominio (core business logic).

REGLAS ESTRICTAS:
- Java 17, sin dependencias de Spring/JPA/Lombok en esta capa
- Inmutabilidad preferida para Value Objects y Events
- Optional<> para retornos nullable
- Javadoc en clases públicas
- Tell, Don't Ask en entidades
- Nombres en español para negocio

PAQUETE BASE: com.helpdesk.domain

TAREAS:

1. Crear los enums en com.helpdesk.domain.enums:
   - EstadoIncidencia (ABIERTO, ASIGNADO, EN_PROGRESO, RESUELTO, CERRADO)
   - NivelPrioridad (BAJA=24h respuesta/120h resolución, MEDIA=8/48, ALTA=2/8, CRITICA=1/4)
   - RolUsuario (CLIENTE, TECNICO, ADMINISTRADOR) con lista de permisos String
   - TipoNotificacion (CREACION_TICKET, ASIGNACION, CAMBIO_ESTADO, RESOLUCION, SLA_VIOLADO, COMENTARIO_NUEVO)

2. Crear los eventos de dominio inmutables en com.helpdesk.domain.event:
   - TicketCreadoEvent (ticketId, clienteId, fechaCreacion, prioridad)
   - EstadoCambiadoEvent (ticketId, estadoAnterior, estadoNuevo, usuarioId, fechaCambio, motivo)
   - TicketAsignadoEvent (ticketId, tecnicoId, asignadoPorId, fechaAsignacion)
   - TicketResueltoEvent (ticketId, tecnicoId, solucion, fechaResolucion)
   - SlaVioladoEvent (ticketId, prioridad, fechaVencimiento, tipoSla)

3. Crear Value Objects en com.helpdesk.domain.valueobject:
   - Email (validación de formato email)
   - Prioridad (wrapper sobre NivelPrioridad con validaciones)

4. Crear las entidades de dominio en com.helpdesk.domain.entity con MÉTODOS DE DOMINIO (no solo getters/setters):

   Incidencia:
   - atributos: id(Long), titulo(String), descripcion(String), fechaCreacion(LocalDateTime), fechaAsignacion(LocalDateTime), fechaResolucion(LocalDateTime), fechaCierre(LocalDateTime), estado(EstadoIncidencia), nivelPrioridad(NivelPrioridad), solucion(String), tecnicoAsignadoId(Long), clienteId(Long), acuerdoServicioId(Long), slaViolado(boolean)
   - métodos: asignar(Long tecnicoId, LocalDateTime fecha), iniciarTrabajo(), resolver(String solucion, LocalDateTime fecha), cerrar(LocalDateTime fecha), reabrir(String motivo), escalarPrioridad(NivelPrioridad nueva), estaVencida(LocalDateTime ahora, AcuerdoServicio sla), marcarSlaViolado(), agregarComentario(Comentario c), registrarHistorial(HistorialEstado h)
   - VALIDACIONES: transiciones de estado válidas (solo ADMIN puede saltar estados)

   Usuario:
   - atributos: id(Long), nombre(String), correoElectronico(String), telefono(String), contrasenaHash(String), activo(boolean), rol(RolUsuario), fechaRegistro(LocalDateTime), ultimoAcceso(LocalDateTime)
   - métodos: activar(), desactivar(), actualizarUltimoAcceso(LocalDateTime), tienePermiso(String permiso), validarCredenciales(String password, PasswordEncoder encoder), cambiarRol(RolUsuario nuevoRol)

   Rol:
   - atributos: id(Long), tipo(RolUsuario), permisos(List<String>), descripcion(String)
   - métodos: otorgarPermiso(String), revocarPermiso(String), tienePermiso(String)

   Asignacion:
   - atributos: id(Long), incidenciaId(Long), tecnicoId(Long), asignadoPorId(Long), fechaAsignacion(LocalDateTime), motivo(String), activa(boolean)
   - métodos: desactivar(), esAutomatica()

   AcuerdoServicio:
   - atributos: id(Long), nombre(String), descripcion(String), nivelPrioridad(NivelPrioridad), tiempoMaxRespuestaHoras(int), tiempoMaxResolucionHoras(int), activo(boolean), fechaCreacion(LocalDateTime)
   - métodos: calcularFechaLimiteRespuesta(LocalDateTime inicio), calcularFechaLimiteResolucion(LocalDateTime inicio), estaActivo()

   Notificacion:
   - atributos: id(Long), destinatarioId(Long), tipo(TipoNotificacion), asunto(String), mensaje(String), fechaEnvio(LocalDateTime), estadoEnvio(String), leida(boolean)
   - métodos: marcarEnviado(), marcarFallido(String error), marcarLeida()

   HistorialEstado:
   - atributos: id(Long), incidenciaId(Long), estadoAnterior(EstadoIncidencia), estadoNuevo(EstadoIncidencia), fechaCambio(LocalDateTime), usuarioCambioId(Long), motivo(String)
   - métodos: static crear(Long incidenciaId, EstadoIncidencia anterior, EstadoIncidencia nuevo, Long usuarioId, String motivo)

   Comentario:
   - atributos: id(Long), incidenciaId(Long), autorId(Long), contenido(String), fechaHora(LocalDateTime), visibleParaCliente(boolean)
   - métodos: editar(String nuevoContenido), ocultarParaCliente()

5. Crear excepciones de dominio en com.helpdesk.domain.exception:
   - DomainException (base)
   - TransicionEstadoInvalidaException
   - SlaVioladoException
   - UsuarioNoAutorizadoException

IMPORTANTE:
- NO uses Lombok en esta capa (constructores explícitos)
- NO importes nada de Spring o JPA
- Las entidades deben ser POJOs puros con lógica de negocio
- Incluye Javadoc en cada clase pública
- Genera tests unitarios básicos para las transiciones de estado de Incidencia
```

---

### 🟦 FASE 2: DOMINIO PORTS (Interfaces de Repositorio + Domain Services)
**Rama:** `feature/F2-dominio-ports`
**Depende de:** F1

```
CONTEXT:
FASE 2 del sistema HelpDesk. Ya tengo las entidades, enums y eventos de dominio creados.
Ahora necesito los PUERTOS (interfaces) y SERVICIOS DE DOMINIO.

REGLAS:
- Interfaces puras, sin anotaciones Spring/JPA
- Optional<> para retornos nullable
- Nombres descriptivos en español/inglés técnico
- Javadoc en interfaces públicas

PAQUETE BASE: com.helpdesk.domain

TAREAS:

1. Crear interfaces de repositorio en com.helpdesk.domain.repository:

   IncidenciaRepository:
   - Incidencia save(Incidencia incidencia)
   - Optional<Incidencia> findById(Long id)
   - List<Incidencia> findAll()
   - Page<Incidencia> findByEstado(EstadoIncidencia estado, Pageable pageable)
   - Page<Incidencia> findByClienteId(Long clienteId, Pageable pageable)
   - Page<Incidencia> findByTecnicoAsignadoId(Long tecnicoId, Pageable pageable)
   - List<Incidencia> findByEstadoAndFechaCreacionBefore(EstadoIncidencia estado, LocalDateTime fecha)
   - List<Incidencia> findVencidas(LocalDateTime ahora)
   - boolean existsById(Long id)
   - long countByEstado(EstadoIncidencia estado)
   - void deleteById(Long id)

   UsuarioRepository:
   - Usuario save(Usuario usuario)
   - Optional<Usuario> findById(Long id)
   - Optional<Usuario> findByCorreoElectronico(String email)
   - List<Usuario> findByRol(RolUsuario rol)
   - List<Usuario> findActivos()
   - Page<Usuario> findAll(Pageable pageable)
   - boolean existsByCorreoElectronico(String email)
   - void deleteById(Long id)

   AsignacionRepository:
   - Asignacion save(Asignacion asignacion)
   - Optional<Asignacion> findById(Long id)
   - List<Asignacion> findByIncidenciaId(Long incidenciaId)
   - Optional<Asignacion> findActivaByIncidenciaId(Long incidenciaId)
   - List<Asignacion> findByTecnicoId(Long tecnicoId)
   - void desactivarAsignacionesAnteriores(Long incidenciaId)

   AcuerdoServicioRepository:
   - AcuerdoServicio save(AcuerdoServicio acuerdo)
   - Optional<AcuerdoServicio> findById(Long id)
   - List<AcuerdoServicio> findAllActivos()
   - Optional<AcuerdoServicio> findActivoByNivelPrioridad(NivelPrioridad prioridad)
   - void deleteById(Long id)

   ComentarioRepository:
   - Comentario save(Comentario comentario)
   - Optional<Comentario> findById(Long id)
   - List<Comentario> findByIncidenciaId(Long incidenciaId)
   - List<Comentario> findByIncidenciaIdAndVisibleParaClienteTrue(Long incidenciaId)
   - void deleteById(Long id)

   HistorialEstadoRepository:
   - HistorialEstado save(HistorialEstado historial)
   - List<HistorialEstado> findByIncidenciaIdOrderByFechaCambioDesc(Long incidenciaId)
   - Optional<HistorialEstado> findUltimoByIncidenciaId(Long incidenciaId)

   NotificacionRepository:
   - Notificacion save(Notificacion notificacion)
   - List<Notificacion> findPendientes()
   - List<Notificacion> findByDestinatarioId(Long destinatarioId)
   - void marcarEnviado(Long notificacionId)
   - void marcarFallido(Long notificacionId, String error)

2. Crear servicios de dominio en com.helpdesk.domain.service:

   AsignacionService:
   - Long determinarTecnicoAutomatico(NivelPrioridad prioridad, List<Usuario> tecnicosActivos)
   - boolean requiereAsignacionManual(NivelPrioridad prioridad)
   - validarDisponibilidadTecnico(Long tecnicoId)

   SlaService:
   - boolean estaVencida(Incidencia incidencia, AcuerdoServicio acuerdo, LocalDateTime ahora)
   - LocalDateTime calcularFechaLimiteRespuesta(LocalDateTime inicio, int horas)
   - LocalDateTime calcularFechaLimiteResolucion(LocalDateTime inicio, int horas)
   - boolean esHorarioHabil(LocalDateTime fecha)
   - void validarCambioPrioridad(Incidencia incidencia, NivelPrioridad nueva)

IMPORTANTE:
- Estas son interfaces puras (puertos). La implementación vendrá en infraestructura.
- NO uses @Repository u otras anotaciones Spring aquí.
- Documenta cada método con Javadoc.
```

---

### 🟨 FASE 3: CAPA DE APLICACIÓN (DTOs, Commands, Use Cases, Services)
**Rama:** `feature/F3-aplicacion`
**Depende de:** F2

```
CONTEXT:
FASE 3 del sistema HelpDesk. Ya tengo dominio completo (entidades, eventos, repositorios interfaces).
Necesito la capa de APLICACIÓN que orquesta los casos de uso con CQRS.

REGLAS:
- Java 17, Spring Boot 3.2
- Separación CQRS: Commands (escritura) vs Queries (lectura)
- DTOs inmutables (records o clases con builder)
- Jakarta Validation (@NotNull, @NotBlank, @Email, @Size) en request DTOs
- Inyección por constructor
- Optional<> en retornos
- Javadoc

PAQUETE BASE: com.helpdesk.application

TAREAS:

1. Crear DTOs de Request en com.helpdesk.application.dto.request:
   - CrearIncidenciaRequestDTO (titulo, descripcion, prioridad)
   - AsignarTicketRequestDTO (tecnicoId, motivo)
   - ResolverTicketRequestDTO (solucion)
   - ReabrirTicketRequestDTO (motivo)
   - CambiarPrioridadRequestDTO (nuevaPrioridad, motivo)
   - CrearComentarioRequestDTO (contenido, visibleCliente)
   - EditarComentarioRequestDTO (contenido)
   - LoginRequestDTO (email, password)
   - RegistroRequestDTO (nombre, email, password, telefono)
   - CrearUsuarioRequestDTO (nombre, email, password, telefono, rol)
   - ActualizarUsuarioRequestDTO (nombre, telefono, activo)
   - CambiarRolRequestDTO (nuevoRol)
   - CrearAcuerdoServicioRequestDTO (nombre, descripcion, nivelPrioridad, tiempoMaxRespuestaHoras, tiempoMaxResolucionHoras)

2. Crear DTOs de Response en com.helpdesk.application.dto.response:
   - IncidenciaResponseDTO (id, titulo, descripcion, estado, prioridad, fechas, clienteId, tecnicoAsignadoId)
   - IncidenciaSummaryDTO (id, titulo, estado, prioridad, fechaCreacion) para listados
   - IncidenciaDetalleDTO (extends Summary + descripcion + solucion + historial + comentarios)
   - UsuarioResponseDTO (id, nombre, email, telefono, rol, activo, fechaRegistro)
   - JwtResponseDTO (token, tipo, expiracion)
   - ComentarioDTO (id, contenido, autorId, fechaHora, visibleParaCliente)
   - HistorialEstadoDTO (id, estadoAnterior, estadoNuevo, fechaCambio, usuarioId, motivo)
   - AcuerdoServicioDTO (id, nombre, prioridad, tiempos, activo)
   - NotificacionDTO (id, asunto, mensaje, tipo, estadoEnvio, leida)
   - DashboardMetricsDTO (totalIncidencias, abiertas, resueltas, cerradas, slaViolados, promedioResolucionHoras)
   - SlaCumplimientoDTO (periodo, total, cumplidos, violados, porcentajeCumplimiento)
   - TecnicoPerformanceDTO (tecnicoId, nombre, asignadas, resueltas, promedioHorasResolucion)
   - ApiErrorDTO (timestamp, status, error, message, path)

3. Crear Commands en com.helpdesk.application.command:
   - CrearIncidenciaCommand (clienteId, titulo, descripcion, prioridad)
   - AsignarTicketCommand (incidenciaId, tecnicoId, asignadoPorId, motivo)
   - IniciarTrabajoCommand (incidenciaId, tecnicoId)
   - ResolverTicketCommand (incidenciaId, tecnicoId, solucion)
   - CerrarTicketCommand (incidenciaId, usuarioId)
   - ReabrirTicketCommand (incidenciaId, clienteId, motivo)
   - CambiarPrioridadCommand (incidenciaId, adminId, nuevaPrioridad, motivo)
   - CrearComentarioCommand (incidenciaId, autorId, contenido, visibleCliente)
   - CrearUsuarioCommand (nombre, email, password, telefono, rol)
   - ActualizarUsuarioCommand (usuarioId, nombre, telefono, activo)
   - CambiarRolCommand (usuarioId, adminId, nuevoRol)
   - CrearAcuerdoServicioCommand (...)

4. Crear Use Cases / Servicios de Aplicación en com.helpdesk.application.usecase:
   (Cada use case es una clase con un método execute())

   IncidenciaUseCase:
   - IncidenciaResponseDTO crear(CrearIncidenciaCommand command)
   - IncidenciaResponseDTO asignar(AsignarTicketCommand command)
   - IncidenciaResponseDTO iniciarTrabajo(IniciarTrabajoCommand command)
   - IncidenciaResponseDTO resolver(ResolverTicketCommand command)
   - IncidenciaResponseDTO cerrar(CerrarTicketCommand command)
   - IncidenciaResponseDTO reabrir(ReabrirTicketCommand command)
   - IncidenciaResponseDTO cambiarPrioridad(CambiarPrioridadCommand command)
   - Page<IncidenciaSummaryDTO> listarTodas(Pageable pageable) // Admin
   - Page<IncidenciaSummaryDTO> listarPorCliente(Long clienteId, Pageable pageable)
   - Page<IncidenciaSummaryDTO> listarPorTecnico(Long tecnicoId, Pageable pageable)
   - Optional<IncidenciaDetalleDTO> obtenerDetalle(Long incidenciaId)

   AutenticacionUseCase:
   - JwtResponseDTO login(LoginRequestDTO request)
   - JwtResponseDTO refresh(String refreshToken)
   - UsuarioResponseDTO registrar(RegistroRequestDTO request)

   UsuarioUseCase:
   - UsuarioResponseDTO crear(CrearUsuarioCommand command)
   - UsuarioResponseDTO actualizar(ActualizarUsuarioCommand command)
   - UsuarioResponseDTO cambiarRol(CambiarRolCommand command)
   - Page<UsuarioResponseDTO> listarTodos(Pageable pageable)
   - Optional<UsuarioResponseDTO> obtenerPorId(Long id)
   - void eliminar(Long id)

   ComentarioUseCase:
   - ComentarioDTO agregar(CrearComentarioCommand command)
   - ComentarioDTO editar(Long comentarioId, EditarComentarioRequestDTO dto, Long autorId)
   - void eliminar(Long comentarioId, Long solicitanteId)
   - List<ComentarioDTO> listarPorIncidencia(Long incidenciaId)

   ReporteUseCase:
   - DashboardMetricsDTO obtenerDashboard()
   - SlaCumplimientoDTO obtenerSlaCumplimiento(LocalDateTime desde, LocalDateTime hasta)
   - List<TecnicoPerformanceDTO> obtenerRendimientoTecnicos(LocalDateTime desde, LocalDateTime hasta)
   - Map<String, Long> contarPorEstado(LocalDateTime desde, LocalDateTime hasta)

5. Crear servicios de aplicación auxiliares en com.helpdesk.application.service:
   - NotificacionApplicationService (orquesta envío de emails basado en eventos)
   - IncidenciaQueryService (lecturas optimizadas, proyecciones)

IMPORTANTE:
- Los use cases DEBEN validar reglas de negocio delegando a entidades de dominio
- Usar @Transactional en métodos que escriben
- Publicar eventos de dominio después de persistir
- NO exponer entidades de dominio. Usar DTOs.
- Records para DTOs simples, clases con @Builder para complejos
```

---

### 🟩 FASE 4: INFRAESTRUCTURA - PERSISTENCIA (JPA + Mappers)
**Rama:** `feature/F4-infra-jpa`
**Depende de:** F3

```
CONTEXT:
FASE 4A: Infraestructura de persistencia. Necesito implementar los puertos de repositorio con JPA/Hibernate.

REGLAS:
- Spring Data JPA + Hibernate 6
- Entidades JPA separadas de entidades de dominio (no mezclar)
- MapStruct para mapeo entre JPA Entity ↔ Domain Entity
- Lombok permitido (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- Inyección por constructor
- Optional<> en queries
- ddl-auto=validate (no generar schema desde JPA)

PAQUETE BASE: com.helpdesk.infrastructure.persistence

TAREAS:

1. Crear entidades JPA en com.helpdesk.infrastructure.persistence.entity:
   - IncidenciaJpaEntity (con @Entity, @Table, @Id @GeneratedValue, @Enumerated(EnumType.STRING), relaciones @ManyToOne/@OneToMany donde aplique)
   - UsuarioJpaEntity
   - RolJpaEntity
   - AsignacionJpaEntity
   - AcuerdoServicioJpaEntity
   - ComentarioJpaEntity
   - HistorialEstadoJpaEntity
   - NotificacionJpaEntity

   NOTA: Usar FetchType.LAZY en relaciones. Indexar columnas de búsqueda frecuente (estado, clienteId, tecnicoId, email).

2. Crear Spring Data Repositories en com.helpdesk.infrastructure.persistence.repository:
   - SpringDataIncidenciaRepository extends JpaRepository<IncidenciaJpaEntity, Long>
   - SpringDataUsuarioRepository extends JpaRepository<UsuarioJpaEntity, Long>
   - SpringDataAsignacionRepository
   - SpringDataAcuerdoServicioRepository
   - SpringDataComentarioRepository
   - SpringDataHistorialEstadoRepository
   - SpringDataNotificacionRepository

   (Con métodos derivados + @Query nativos donde sea complejo)

3. Crear Adaptadores en com.helpdesk.infrastructure.persistence.repository:
   - JpaIncidenciaRepositoryAdapter implements IncidenciaRepository
   - JpaUsuarioRepositoryAdapter implements UsuarioRepository
   - JpaAsignacionRepositoryAdapter implements AsignacionRepository
   - JpaAcuerdoServicioRepositoryAdapter implements AcuerdoServicioRepository
   - JpaComentarioRepositoryAdapter implements ComentarioRepository
   - JpaHistorialEstadoRepositoryAdapter implements HistorialEstadoRepository
   - JpaNotificacionRepositoryAdapter implements NotificacionRepository

   Cada adaptador:
   - Inyecta el SpringDataRepository correspondiente
   - Usa MapStruct mapper para convertir JPA ↔ Domain
   - Implementa TODOS los métodos de la interfaz de dominio
   - Usa Optional<> en retornos
   - Maneja paginación con Pageable

4. Crear Mappers MapStruct en com.helpdesk.infrastructure.persistence.mapper:
   - IncidenciaPersistenceMapper
   - UsuarioPersistenceMapper
   - AsignacionPersistenceMapper
   - AcuerdoServicioPersistenceMapper
   - ComentarioPersistenceMapper
   - HistorialEstadoPersistenceMapper
   - NotificacionPersistenceMapper

   (Mapeo de enums String, fechas, colecciones)

5. Crear archivo data.sql para datos iniciales:
   - 1 Admin (admin@helpdesk.com / admin123)
   - 2 Técnicos (tecnico1@helpdesk.com, tecnico2@helpdesk.com)
   - 2 Clientes (cliente1@helpdesk.com, cliente2@helpdesk.com)
   - 1 Acuerdo de Servicio por cada nivel de prioridad
   - Contraseñas hasheadas con BCrypt (usa $2a$10$... hashes de ejemplo)

IMPORTANTE:
- NUNCA retornar JpaEntity desde la capa de aplicación
- Todos los métodos de adaptador deben ser @Transactional donde aplique
- Usar @Component en adaptadores
- Incluye tests de integración @DataJpaTest para cada adaptador
```

---

### 🟩 FASE 5: INFRAESTRUCTURA - ADAPTERS (Security, Mail, Events, Config)
**Rama:** `feature/F5-infra-adapters`
**Depende de:** F4

```
CONTEXT:
FASE 4B/5: Infraestructura de adapters externos: Seguridad JWT, SMTP Mail, Event Publisher, y Configuración Spring.

REGLAS:
- Spring Security 6 (filter chain funcional)
- JWT con io.jsonwebtoken 0.12.3
- SMTP con Spring Mail
- Eventos con ApplicationEventPublisher
- Scheduling con @Scheduled para verificación SLA
- Async con @Async para notificaciones
- Inyección por constructor
- application.yml con variables de entorno ${VAR:default}

PAQUETE BASE: com.helpdesk.infrastructure

TAREAS:

1. SEGURIDAD (com.helpdesk.infrastructure.security):
   - JwtTokenProvider: generarToken(String username, Map claims), validarToken(String token), obtenerUsername(String token), obtenerExpiracion(String token)
   - JwtAuthenticationFilter extends OncePerRequestFilter: extrae token del header Authorization, valida, setea SecurityContext
   - UserDetailsServiceImpl implements UserDetailsService: carga usuario por email desde repositorio
   - CustomUserDetails implements UserDetails: adapta entidad Usuario a UserDetails de Spring
   - SecurityConfig: filterChain con CSRF disabled, STATELESS session, autorización por roles, inyección de JwtAuthenticationFilter
   - PasswordEncoder bean: BCryptPasswordEncoder
   - AuthenticationManager bean

2. NOTIFICACIONES (com.helpdesk.infrastructure.notification):
   - SmtpNotificationAdapter implements NotificationPort (crear esta interfaz en domain si no existe)
   - EmailTemplateService: genera HTML/text para cada TipoNotificacion
   - NotificacionQueueProcessor: procesa notificaciones pendientes cada X minutos (@Scheduled)

3. EVENTOS (com.helpdesk.infrastructure.event):
   - SpringEventPublisher implements DomainEventPublisher (interfaz en domain)
   - TicketEventListener: escucha TicketCreadoEvent, TicketAsignadoEvent, TicketResueltoEvent → crea Notificacion y envía email
   - SlaEventListener: escucha SlaVioladoEvent → alerta admin
   - EstadoCambiadoListener: escucha EstadoCambiadoEvent → guarda historial + notifica
   - Todos los listeners deben ser @Async y @TransactionalEventListener(phase = AFTER_COMMIT)

4. CONFIGURACIÓN (com.helpdesk.infrastructure.config):
   - CorsConfig: CorsConfigurationSource permitiendo orígenes configurables
   - AsyncConfig: ThreadPoolTaskExecutor para @Async
   - SchedulingConfig: @EnableScheduling
   - MailConfig: JavaMailSender bean con propiedades desde application.yml

5. APPLICATION.YML:
   Generar application.yml completo con:
   - server.port=8080, context-path=/api/v1
   - datasource postgresql con HikariCP (pool 20 conexiones)
   - jpa.hibernate.ddl-auto=validate, show-sql=false, dialect PostgreSQLDialect, batch_size=20, open-in-view=false
   - spring.mail host/port/username/password/protocol + smtp auth/starttls
   - jwt.secret (desde env), expiration=86400000, refresh-expiration=604800000
   - logging nivel DEBUG para com.helpdesk
   - management endpoints health, info, metrics

IMPORTANTE:
- SecurityConfig debe permitir /auth/** sin autenticación
- JWT Filter debe ejecutarse antes de UsernamePasswordAuthenticationFilter
- Todos los secrets/passwords desde variables de entorno
- @Profile("dev") para configuraciones de desarrollo si aplica
```

---

### 🟥 FASE 6: PRESENTACIÓN (Controllers, Filters, Exception Handler)
**Rama:** `feature/F6-presentacion`
**Depende de:** F5

```
CONTEXT:
FASE 6: Capa de Presentación. Exponer API REST, manejar excepciones globales, validar requests.

REGLAS:
- Controllers REST con @RestController, @RequestMapping
- ResponseEntity<> obligatorio con códigos HTTP correctos
- Jakarta Validation en Request DTOs (@Valid)
- @ControllerAdvice global
- @PreAuthorize o SecurityFilterChain para roles
- Inyección por constructor
- Javadoc en controllers
- NO lógica de negocio en controllers (solo delegar a use cases)

PAQUETE BASE: com.helpdesk.presentation

TAREAS:

1. CONTROLLERS (com.helpdesk.presentation.controller):

   AuthController (/api/v1/auth):
   - POST /login → ResponseEntity<JwtResponseDTO> (200/401)
   - POST /register → ResponseEntity<UsuarioResponseDTO> (201/400/409)
   - POST /refresh → ResponseEntity<JwtResponseDTO> (200/401)

   IncidenciaController (/api/v1/incidencias):
   - POST / → ResponseEntity<IncidenciaResponseDTO> (201) [CLIENTE, ADMIN]
   - GET / → ResponseEntity<Page<IncidenciaSummaryDTO>> (200) [ADMIN]
   - GET /mis-tickets → ResponseEntity<Page<IncidenciaSummaryDTO>> (200) [CLIENTE]
   - GET /asignadas → ResponseEntity<Page<IncidenciaSummaryDTO>> (200) [TECNICO]
   - GET /{id} → ResponseEntity<IncidenciaDetalleDTO> (200/404)
   - PUT /{id}/asignar → ResponseEntity<IncidenciaResponseDTO> (200/404/409) [ADMIN]
   - PUT /{id}/iniciar → ResponseEntity<IncidenciaResponseDTO> (200/403/404) [TECNICO]
   - PUT /{id}/resolver → ResponseEntity<IncidenciaResponseDTO> (200/403/404) [TECNICO]
   - PUT /{id}/cerrar → ResponseEntity<IncidenciaResponseDTO> (200/404) [CLIENTE, ADMIN]
   - PUT /{id}/reabrir → ResponseEntity<IncidenciaResponseDTO> (200/404) [CLIENTE]
   - PUT /{id}/prioridad → ResponseEntity<IncidenciaResponseDTO> (200/404) [ADMIN]
   - GET /{id}/historial → ResponseEntity<List<HistorialEstadoDTO>> (200/404)
   - GET /{id}/comentarios → ResponseEntity<List<ComentarioDTO>> (200/404)

   ComentarioController (/api/v1/comentarios):
   - POST /incidencias/{id}/comentarios → ResponseEntity<ComentarioDTO> (201) [autorizados]
   - PUT /{id} → ResponseEntity<ComentarioDTO> (200/403/404) [autor]
   - DELETE /{id} → ResponseEntity<Void> (204/403/404) [autor/ADMIN]

   UsuarioController (/api/v1/usuarios):
   - GET / → ResponseEntity<Page<UsuarioResponseDTO>> (200) [ADMIN]
   - GET /{id} → ResponseEntity<UsuarioResponseDTO> (200/404) [ADMIN/propio]
   - POST / → ResponseEntity<UsuarioResponseDTO> (201/400/409) [ADMIN]
   - PUT /{id} → ResponseEntity<UsuarioResponseDTO> (200/404) [ADMIN]
   - PUT /{id}/rol → ResponseEntity<UsuarioResponseDTO> (200/404) [ADMIN]
   - DELETE /{id} → ResponseEntity<Void> (204/404) [ADMIN]

   AcuerdoServicioController (/api/v1/slas):
   - GET / → ResponseEntity<List<AcuerdoServicioDTO>> (200) [ADMIN]
   - GET /{id} → ResponseEntity<AcuerdoServicioDTO> (200/404) [ADMIN]
   - POST / → ResponseEntity<AcuerdoServicioDTO> (201) [ADMIN]
   - PUT /{id} → ResponseEntity<AcuerdoServicioDTO> (200) [ADMIN]
   - DELETE /{id} → ResponseEntity<Void> (204) [ADMIN]

   ReporteController (/api/v1/reportes):
   - GET /dashboard → ResponseEntity<DashboardMetricsDTO> (200) [ADMIN]
   - GET /sla-cumplimiento → ResponseEntity<SlaCumplimientoDTO> (200) [ADMIN]
   - GET /tecnico-rendimiento → ResponseEntity<List<TecnicoPerformanceDTO>> (200) [ADMIN]
   - GET /incidencias-por-estado → ResponseEntity<Map<String, Long>> (200) [ADMIN]

2. EXCEPTION HANDLER (com.helpdesk.presentation.exception):
   - GlobalExceptionHandler con @ControllerAdvice
   - Manejo de:
     - ResourceNotFoundException → 404
     - BusinessException → 409
     - TransicionEstadoInvalidaException → 409
     - AccessDeniedException → 403
     - MethodArgumentNotValidException → 400 (con lista de errores de validación)
     - Exception genérica → 500
   - Respuesta estandarizada: ApiErrorDTO (timestamp, status, error, message, path, errors[])

3. FILTROS (com.helpdesk.presentation.filter):
   - RequestLoggingFilter (opcional): loguea requests/response time
   - (JwtAuthenticationFilter ya está en infraestructura/security)

IMPORTANTE:
- Usar @Validated en nivel de clase controller si es necesario
- @PreAuthorize("hasRole('ADMINISTRADOR')") o hasAnyRole donde aplique
- Documentar con @Operation (OpenAPI) si es posible
- Todos los endpoints deben retornar ResponseEntity
- Tests @WebMvcTest para cada controller con MockMvc
```

---

### 🟪 FASE 7: CONFIGURACIÓN Y TESTS (Integration Tests + Data)
**Rama:** `feature/F7-config-tests`
**Depende de:** F6

```
CONTEXT:
FASE 7: Configuración final, tests de integración, datos de prueba, y validación del sistema completo.

REGLAS:
- Tests unitarios con JUnit 5 + Mockito
- Tests de integración @SpringBootTest con TestRestTemplate o MockMvc
- @DataJpaTest para repositorios
- @WebMvcTest para controllers
- @AutoConfigureMockMvc para integración
- Base de datos H2 para tests
- Maven Surefire/Failsafe configurados

TAREAS:

1. TESTS UNITARIOS (src/test/java):

   Dominio:
   - IncidenciaTest: probar transiciones de estado válidas e inválidas
   - UsuarioTest: validar permisos, activación/desactivación
   - AsignacionTest: verificar desactivación
   - SlaServiceTest: cálculo de fechas límite, horarios hábiles

   Aplicación:
   - IncidenciaUseCaseTest: Mockito de repositorios, verificar publicación de eventos
   - AutenticacionUseCaseTest: login exitoso/fallido, registro duplicado
   - UsuarioUseCaseTest: CRUD con mocks
   - ComentarioUseCaseTest: agregar/editar/eliminar

2. TESTS DE INTEGRACIÓN:

   - IncidenciaRepositoryIntegrationTest: @DataJpaTest, guardar y recuperar incidencia, búsquedas por estado/cliente/técnico
   - UsuarioRepositoryIntegrationTest: búsqueda por email, existencia
   - AuthControllerIntegrationTest: @SpringBootTest + MockMvc, login retorna JWT, register crea usuario
   - IncidenciaControllerIntegrationTest: flujo completo crear→asignar→resolver→cerrar
   - SlaIntegrationTest: verificar que scheduler detecta tickets vencidos

3. DATOS DE PRUEBA:
   - src/test/resources/application-test.yml (H2, logging debug)
   - src/test/resources/data-test.sql (usuarios, acuerdos, incidencias de prueba)

4. CONFIGURACIÓN MAVEN:
   - pom.xml completo (ya debería existir, verificar dependencias)
   - Surefire plugin para unit tests
   - Failsafe plugin para integration tests (clases *IT.java)

5. VERIFICACIÓN:
   - mvn clean test (debe pasar 100%)
   - mvn clean verify (incluye integration tests)
   - Cobertura mínima: Dominio 90%, Aplicación 80%, Infraestructura 70%

IMPORTANTE:
- Usar @TestMethodOrder(MethodOrderer.OrderAnnotation.class) para tests de flujo
- @Sql(scripts = "/data-test.sql", executionPhase = BEFORE_TEST_METHOD) donde aplique
- Limpiar contexto de seguridad después de cada test (@AfterEach)
- Tests de flujo completo: crear incidencia → asignar → resolver → cerrar → verificar historial
```

---

### 🟫 FASE 8: INTEGRACIÓN FINAL Y QA
**Rama:** `feature/F8-integracion`
**Depende de:** F7

```
CONTEXT:
FASE 8: Integración final, ajustes, documentación y preparación para release.

TAREAS:

1. AJUSTES Y REFACTORIZACIÓN:
   - Revisar TODOs en código
   - Verificar que no hay @Autowired en campos
   - Verificar que dominio no importa Spring/JPA
   - Revisar logs (quitar sysouts, usar SLF4J)
   - Verificar manejo de excepciones (no stack traces en producción)
   - Revisar CORS para producción (no permitir *)

2. DOCUMENTACIÓN:
   - README.md con:
     - Descripción del proyecto
     - Stack tecnológico
     - Instrucciones de instalación (Docker opcional)
     - Estructura de paquetes
     - Endpoints principales
     - Variables de entorno requeridas
     - Cómo ejecutar tests
   - LICENSE (MIT o Apache 2.0)
   - CHANGELOG.md (versiones)

3. POSTMAN COLLECTION:
   - Crear archivo HelpDesk_API.postman_collection.json con:
     - Carpeta Auth (login, register, refresh)
     - Carpeta Incidencias (CRUD + transiciones)
     - Carpeta Usuarios (CRUD admin)
     - Carpeta Reportes (dashboard)
     - Variables de entorno {{base_url}}, {{token}}
     - Tests de Postman para verificar códigos HTTP

4. DOCKER (OPCIONAL):
   - Dockerfile multistage (build con Maven, runtime con JRE 17)
   - docker-compose.yml con PostgreSQL + App + pgAdmin (opcional)
   - .dockerignore

5. RELEASE CHECKLIST:
   - mvn clean package (genera JAR ejecutable)
   - java -jar target/helpdesk-api-1.0.0.jar (verificar arranque)
   - Verificar endpoints con curl/Postman
   - Merge feature/F8-integracion → develop
   - Crear PR develop → main
   - Tag v1.0.0 en main

IMPORTANTE:
- Esta fase es manual/semiautomática. Cursor puede ayudar con README y Dockerfile.
- No generar código nuevo de negocio aquí, solo pulir lo existente.
```

---

## 6. COMANDOS GIT RÁPIDOS

```bash
# Inicializar proyecto
mvn archetype:generate -DgroupId=com.helpdesk -DartifactId=helpdesk-api -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
cd helpdesk-api
rm -rf src/main/java/com/helpdesk/App.java src/test/java/com/helpdesk/AppTest.java
git init
git add .
git commit -m "init: proyecto base Maven"
git checkout -b develop
git branch -M main

# Feature branch
git checkout -b feature/F1-dominio-core
# ... desarrollo ...
git add .
git commit -m "feat: entidades de dominio y enums"
git checkout develop
git merge --no-ff feature/F1-dominio-core -m "merge: dominio core"
git branch -d feature/F1-dominio-core

# Release
git checkout main
git merge --no-ff develop -m "release: v1.0.0"
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin main --tags
```

---

## 7. CHECKLIST DE VALIDACIÓN

### Antes de cada commit
- [ ] Compila: `mvn clean compile`
- [ ] Tests pasan: `mvn clean test`
- [ ] Sin errores SpotBugs/Checkstyle (si aplica)
- [ ] Sin @Autowired en campos
- [ ] Sin imports de Spring/JPA en capa dominio
- [ ] Javadoc en clases públicas
- [ ] Optional<> usado correctamente
- [ ] ResponseEntity<> en controllers

### Antes de merge a develop
- [ ] Review de código propio (diff completo)
- [ ] Tests de integración pasan
- [ ] No hay passwords hardcodeados
- [ ] application.yml usa variables de entorno
- [ ] Cobertura de tests > 70%

### Antes de release (main)
- [ ] Todos los endpoints probados con Postman
- [ ] Flujo completo de incidencia funciona (crear→asignar→resolver→cerrar)
- [ ] Notificaciones por email funcionan (o se degradan graceful)
- [ ] JWT expira correctamente
- [ ] Roles y permisos funcionan
- [ ] README completo
- [ ] Docker funciona (si aplica)

---

## 🚀 CÓMO USAR ESTE PROMPT MASTER

1. **Prepara tu entorno:**
   - Instala Cursor (cursor.sh)
   - Crea proyecto Maven con Spring Initializr (Java 17, Spring Boot 3.2, deps: Web, Data JPA, Security, Validation, Mail, PostgreSQL)
   - Configura Git Flow: `git flow init` (o manualmente ramas main/develop)

2. **Ejecuta fase por fase:**
   - Crea la rama feature correspondiente
   - Copia el bloque de prompt de esa fase (incluyendo CONTEXT y TAREAS)
   - Pégalo en Cursor Chat (Ctrl+L) o Composer (Ctrl+I)
   - Revisa el código generado, corrige si es necesario
   - Commitea con mensaje convencional
   - Mergea a develop cuando esté listo

3. **No saltes fases.** Cada fase depende de la anterior.

4. **Si Cursor se confunde:**
   - Pega el CONTEXT completo de nuevo
   - Especifica el paquete exacto (`com.helpdesk.domain.entity`)
   - Menciona las reglas que está rompiendo (`Recuerda: dominio no importa Spring`)

---

**Fin del Prompt Master**
