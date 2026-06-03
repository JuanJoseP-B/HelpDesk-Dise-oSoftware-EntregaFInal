# 🛠️ Help Desk API (Arquitectura Hexagonal)

Sistema Backend API REST para la gestión de incidencias y soporte técnico (Service Desk).

## 📄 Contexto del Proyecto
Este sistema permite la gestión completa del ciclo de vida de incidencias, desde su creación por parte del cliente hasta su resolución y cierre por técnicos o administradores, garantizando el cumplimiento de los Acuerdos de Nivel de Servicio (SLAs).

### 👥 Actores
- **Cliente:** Crea incidencias, consulta estado, recibe notificaciones.
- **Técnico:** Gestiona tickets asignados, añade comentarios, resuelve incidencias.
- **Administrador:** Gestión de usuarios, configuración de SLAs, asignación manual, reportes.

### 🔄 Flujo de Estados
ABIERTO ➔ ASIGNADO ➔ EN_PROGRESO ➔ RESUELTO ➔ CERRADO

## 📐 Diseño y Arquitectura

### 1. Diagrama de Contexto (C4 - Nivel 1)
Muestra la interacción del sistema con los actores humanos y los sistemas externos (LDAP, SMTP, Monitoreo).
<img width="551" height="399" alt="diagramaDeContexto drawio" src="https://github.com/user-attachments/assets/b4406150-ffb2-4037-be38-42e96cc8d31d" />

### 2. Arquitectura Hexagonal
El proyecto sigue los principios de la **Arquitectura Hexagonal (Ports & Adapters)**:
- **Dominio:** Lógica de negocio pura (Entidades, Value Objects, Puertos, Eventos).
- **Aplicación:** Casos de uso y orquestación (CQRS).
- **Infraestructura:** Adaptadores técnicos (JPA, JWT, Mail, Spring).
- **Presentación:** Adaptadores de entrada (REST Controllers).

### 3. Diagrama de Clases UML (Backend)
Representación de la Programación Orientada a Objetos en el core de la aplicación, implementando patrones de diseño para el manejo de estados.
![DiagramaClasesV2](https://github.com/user-attachments/assets/462c4d9b-b9f6-491b-8c8c-18c7ffa8d369)

## 💻 Stack Tecnológico
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA (PostgreSQL)**
- **Spring Security + JWT**
- **Spring Mail (SMTP)**
- **MapStruct & Lombok** (Lombok solo en infra/presentación)
- **JUnit 5 & Mockito**
- **Maven**

## 📂 Estructura de Paquetes
```
com.helpdesk
+-- application       # Casos de uso, DTOs, Commands
+-- domain            # Entidades, Enums, Puertos de Repositorio
+-- infrastructure    # Implementaciones JPA, Security, Mail, Config
+-- presentation      # Controllers REST, Exception Handling
```

## 🚀 Configuración e Instalación

### Requisitos
- JDK 17
- Maven 3.8+
- PostgreSQL 15+

### Variables de Entorno
Configure las siguientes variables en su entorno o en `src/main/resources/application.yml`:
- `DB_URL`: URL de la base de datos PostgreSQL.
- `DB_USERNAME`: Usuario de la DB.
- `DB_PASSWORD`: Contraseña de la DB.
- `JWT_SECRET`: Clave secreta para tokens JWT.
- `MAIL_HOST`: Servidor SMTP.
- `MAIL_USERNAME`: Usuario SMTP.
- `MAIL_PASSWORD`: Contraseña SMTP.

### Ejecución
```bash
mvn clean install
mvn spring-boot:run
```

## 🧪 Tests
Para ejecutar la suite de pruebas completa:
```bash
mvn clean verify
```
Esto ejecutará tanto los tests unitarios (`*Test.java`) como los de integración (`*IT.java`).

## 📄 Licencia
Este proyecto está bajo la licencia MIT - vea el archivo [LICENSE](LICENSE) para detalles.
