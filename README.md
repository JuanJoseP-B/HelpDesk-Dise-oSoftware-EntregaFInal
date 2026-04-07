# 🛠️ Help Desk Ticketing System

Un sistema de gestión de incidencias (Help Desk) de nivel intermedio diseñado para optimizar el soporte técnico interno. Este proyecto permite a los empleados reportar problemas, a los técnicos gestionar y resolver tickets, y a los administradores supervisar el flujo de trabajo general.

## 📋 Tabla de Contenidos
- [Características Principales](#-características-principales)
- [Arquitectura y Diseño](#-arquitectura-y-diseño)
- [Stack Tecnológico](#-stack-tecnológico)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Instalación y Despliegue (Local)](#-instalación-y-despliegue-local)
- [Flujo de Trabajo (GitFlow)](#-flujo-de-trabajo-gitflow)

## 🚀 Características Principales

* **Gestión de Roles:** Soporte para tres tipos de usuarios (Cliente, Técnico, Administrador) con permisos específicos.
* **Ciclo de Vida del Ticket:** Trazabilidad completa del estado de una incidencia (`Abierto` -> `Asignado` -> `En Progreso` -> `Resuelto` -> `Cerrado`).
* **Asignación Inteligente:** Flujo automatizado para asignar tickets a técnicos disponibles según la categoría del problema.
* **Integraciones Externas:** Autenticación mediante Directorio LDAP corporativo y notificaciones vía correo electrónico (SMTP).

## 🏗️ Arquitectura y Diseño

El sistema ha sido diseñado priorizando la escalabilidad y la separación de responsabilidades. A continuación se detallan los diagramas clave que fundamentan la lógica del negocio:

### 1. Diagrama de Contexto (C4 - Nivel 1)
Muestra la interacción del sistema con los actores humanos y los sistemas externos (LDAP, SMTP, Monitoreo).
![Diagrama de Contexto](<img width="551" height="399" alt="diagramaDeContexto drawio" src="https://github.com/user-attachments/assets/b4406150-ffb2-4037-be38-42e96cc8d31d" />) 


### 2. Diagrama Entidad-Relación (Base de Datos)
Estructura relacional de los datos, asegurando la integridad entre Usuarios, Tickets, Categorías y Comentarios.
![Diagrama ER](docs/images/er-diagram.png)

### 3. Diagrama de Clases UML (Backend)
Representación de la Programación Orientada a Objetos en el core de la aplicación, implementando patrones de diseño para el manejo de estados.
![Diagrama de Clases](docs/images/class-diagram.png)

### 4. Flujo de Estados del Ticket
Ciclo de vida que dicta cómo interactúan el cliente y el técnico a lo largo de la resolución de la incidencia.
![Diagrama de Estados](docs/images/state-machine.png)

## 💻 Stack Tecnológico

**Backend:**
* **Java:** Lógica principal.
* **Framework:** Spring Boot (Web, Data JPA, Security).
* **Base de Datos:** PostgreSQL.
* **Autenticación:** Spring Security LDAP / JWT.

**Frontend:**
* **Lenguaje:** TypeScript.
* **Framework/Librería:** [Aquí pones React, Angular o Vue según elijas].
* **Estilos:** [Ej: Tailwind CSS / Material-UI].

## ⚙️ Instalación y Despliegue (Local)

### Requisitos previos
* Java Development Kit (JDK) 17 o superior.
* Node.js (v18+) y npm/yarn.
* PostgreSQL instalado y corriendo.

### Pasos
1. Clona el repositorio:
   ```bash
   git clone [https://github.com/tu-usuario/help-desk-system.git](https://github.com/tu-usuario/help-desk-system.git)
