# Car Rental — Backend API

REST API for a car rental platform, built with **Spring Boot 3.4.2** and **Java 21**.
Follows **Hexagonal Architecture** (Ports & Adapters). Stateless JWT authentication.

---

## Table of contents

1. [Project overview](#1-project-overview)
2. [Architecture](#2-architecture)
3. [Prerequisites](#3-prerequisites)
4. [Running locally (dev)](#4-running-locally-dev)
5. [Database migrations](#5-database-migrations)
6. [Authentication](#6-authentication)
7. [Main endpoints](#7-main-endpoints)
8. [Swagger UI](#8-swagger-ui)
9. [Environment variables](#9-environment-variables)

---

## 1. Project overview

| Layer | Responsibility |
|-------|---------------|
| Domain | Business rules, entities, port interfaces — zero framework dependency |
| Application | Use case orchestration, DTOs, commands |
| Infrastructure | JPA adapters, JWT, Spring Security, Flyway |
| Interfaces | REST controllers, request/response DTOs, global exception handler |

The mobile client (`/api/cars`) can browse the fleet without authentication.
Fleet management (`/api/admin/cars`) requires an `ADMIN` JWT token.

---

## 2. Architecture

```
src/main/java/com/mourad/backend/
├── domain/
│   ├── model/          Car, CarStatus, AdminAccount, TokenPair, PageResult
│   ├── port/
│   │   ├── in/         AuthUseCase
│   │   └── out/        CarRepository, AdminAccountRepository, TokenPort, PasswordHashPort
│   └── exception/      CarNotFoundException, InvalidCarStateException, …
├── application/
│   ├── command/        CreateCarCommand, UpdateCarPriceCommand
│   ├── dto/            CarDto
│   ├── port/in/        CreateCarUseCase, DeleteCarUseCase, UpdateCarPriceUseCase,
│   │                   GetCarsUseCase, GetCarByIdUseCase
│   └── service/        one @Service per use case + AuthService
├── infrastructure/
│   ├── config/         OpenApiConfig, AdminSeeder
│   ├── security/       SecurityConfig, JwtTokenProvider, JwtAuthenticationFilter, …
│   └── persistence/    JPA entities, repositories, adapters, mappers
└── interfaces/
    ├── rest/           CarController, PublicCarController, AuthController
    ├── dto/            request & response records (CreateCarRequest, ApiError, …)
    └── exception/      GlobalExceptionHandler
```

Full layer rules → [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)

---

## 3. Prerequisites

| Requirement | Version |
|-------------|---------|
| Java | 21 (LTS) |
| Maven | 3.9+ |
| Database (dev) | H2 in-memory — included, no setup needed |
| Database (prod) | PostgreSQL 15+ |

---

## 4. Running locally (dev)

```bash
# Clone
git clone <repo-url>
cd car-rental-backend

# Run with the dev profile (H2 in-memory, Swagger enabled, debug logs)
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

The server starts on **http://localhost:8080**.

A default admin account is seeded automatically on first start:
- username: `admin`
- password: `admin`

**H2 console** (dev only): http://localhost:8080/h2-console
JDBC URL: `jdbc:h2:mem:car_rental_dev`

---

## 5. Database migrations

Managed by **Flyway**. Migrations run automatically on startup.
Files are in `src/main/resources/db/migration/`.

| Version | Description |
|---------|-------------|
| V1 | Creates `cars` table (initial schema) |
| V2 | Removes `license_plate`, renames price columns, adds `year`, tightens `status` constraint |
| V3 | Creates `admin_accounts` table |

To inspect the migration history:
```bash
./mvnw flyway:info -Dflyway.url=jdbc:h2:mem:car_rental_dev
```

---

## 6. Authentication

The API uses **Bearer JWT** tokens (HMAC-SHA256).

### Login

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}' | jq
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

### Use the token

```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

curl -s http://localhost:8080/api/admin/cars \
  -H "Authorization: Bearer $TOKEN" | jq
```

### Refresh

```bash
curl -s -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<refresh_token>"}' | jq
```

Token expiry:
- Access token: **1 hour** (overridable via `JWT_ACCESS_EXPIRATION_MS`)
- Refresh token: **7 days** (overridable via `JWT_REFRESH_EXPIRATION_MS`)

---

## 7. Main endpoints

### Public — no auth required

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/cars` | List cars (pagination + status filter) |
| `GET` | `/api/cars/{id}` | Get car by ID |

Pagination example:
```
GET /api/cars?page=0&size=10&status=AVAILABLE
```

### Admin — requires `Authorization: Bearer <token>`

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/auth/login` | Obtain JWT tokens |
| `POST` | `/api/auth/refresh` | Refresh access token |
| `POST` | `/api/admin/cars` | Add a car to the fleet |
| `PATCH` | `/api/admin/cars/{id}/price` | Update daily price |
| `DELETE` | `/api/admin/cars/{id}` | Remove a car |

Full payloads and examples → [`docs/API.md`](docs/API.md)

---

## 8. Swagger UI

Available in **dev** profile only (disabled in production):

| URL | Content |
|-----|---------|
| http://localhost:8080/swagger-ui.html | Interactive UI |
| http://localhost:8080/api-docs | Raw OpenAPI JSON |

Click **Authorize** in the UI and paste `Bearer <accessToken>` to test protected routes.

---

## 9. Environment variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `JWT_SECRET` | No (dev) | hardcoded dev key | HMAC-SHA256 secret, min 32 chars. **Must be set in prod.** |
| `JWT_ACCESS_EXPIRATION_MS` | No | `3600000` (1 h) | Access token TTL in ms |
| `JWT_REFRESH_EXPIRATION_MS` | No | `604800000` (7 d) | Refresh token TTL in ms |
| `DATABASE_URL` | **Prod only** | — | JDBC URL (e.g. `jdbc:postgresql://host:5432/car_rental`) |
| `DATABASE_USERNAME` | **Prod only** | — | DB user |
| `DATABASE_PASSWORD` | **Prod only** | — | DB password |
| `DB_POOL_MAX` | No | `10` | HikariCP max pool size |
| `DB_POOL_MIN` | No | `2` | HikariCP min idle connections |

### Running in production

```bash
export JWT_SECRET="a-strong-random-secret-of-at-least-32-characters"
export DATABASE_URL="jdbc:postgresql://localhost:5432/car_rental"
export DATABASE_USERNAME="car_rental_user"
export DATABASE_PASSWORD="s3cr3t"

./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

Or with the JAR:
```bash
java -jar target/car-rental-backend-1.0-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## Tests

```bash
./mvnw test
```

21 tests — service unit tests (Mockito) + persistence integration tests (H2 + Flyway).
