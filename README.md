# 🧩 Ecosistema de Microservicios - Prueba Técnica

Este repositorio contiene la implementación completa de un ecosistema basado en arquitectura de microservicios, desarrollado como parte de una prueba técnica. La solución contempla los siguientes módulos principales:

## 🏗️ Microservicios entregados

- **API Gateway**  
  Encargado de enrutar, controlar acceso y centralizar las peticiones HTTP hacia los microservicios internos.

- **Microservicio de Tarjetas (`microservicio-tarjeta`)**  
  Administra la creación y gestión de tarjetas con monto y fecha de expiración. También emite notificaciones a través de RabbitMQ.

- **Microservicio de Usuarios (`microservicio-usuarios`)**  
  Gestiona la autenticación y los roles de los usuarios, incluyendo la creación, actualización y validación de credenciales.

- **Microservicio de Autenticación (`microservicio-autenticacion`)**  
  Implementa un mecanismo de autenticación basado en JWT, validando usuarios y emitiendo tokens para acceso seguro.

---

## 🚀 Instrucciones de despliegue

### 🐘 Microservicio de Tarjetas

#### Crear base de datos con Docker:

```bash
docker run -d --name postgres_giftcard \
  -e POSTGRES_USER=root_giftcard \
  -e POSTGRES_PASSWORD=root_giftcard \
  -e POSTGRES_DB=giftcard_db \
  -p 5435:5432 postgres
```

#### Script de creación de tablas:

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE cards (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  code VARCHAR(100),
  amount NUMERIC,
  create_date TIMESTAMP,
  expire_date TIMESTAMP
);
```

#### Crear contenedor de RabbitMQ:

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  rabbitmq:3-management
```

---

### 👤 Microservicio de Usuarios

#### Crear base de datos con Docker:

```bash
docker run -d --name postgres_user \
  -e POSTGRES_USER=root_user \
  -e POSTGRES_PASSWORD=root_user \
  -e POSTGRES_DB=user_db \
  -p 5436:5432 postgres
```

#### Script de creación de tablas:

```sql
CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

-- Usuario por defecto
INSERT INTO public.users (username, "password", enabled) VALUES
('jesus','{bcrypt}$2a$10$t8MENcxeSUMHHtzstiBgGuexJu.ohyGdFmbeh7xpm/C9QLMcNvKvi', true);

INSERT INTO public.authorities (username, authority) VALUES
('jesus','ROLE_USER');
```

---

## ✅ Pruebas unitarias

Se desarrollaron pruebas unitarias exhaustivas para validar la lógica de negocio de los principales casos de uso, cumpliendo con los criterios técnicos exigidos. Se garantizó:

- Cobertura de pruebas con `JUnit5` y `StepVerifier`.
- Mocking de dependencias utilizando `Mockito`.
- Validación de condiciones de éxito y fallos controlados.

---

## 📊 Documentación técnica adicional

- Se incluye **diagrama del ecosistema** generado en `draw.io` para facilitar la comprensión de la arquitectura, interacciones entre microservicios y mensajería con RabbitMQ.

---
