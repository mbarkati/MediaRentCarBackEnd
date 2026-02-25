# Architecture

## Overview

This project follows **Hexagonal Architecture** (also called Ports & Adapters), as defined by Alistair Cockburn.

The core idea: **business logic has no dependency on any framework or infrastructure**.
The application core (domain + application) can be tested in isolation, and every external system (DB, HTTP, JWT) is hidden behind an interface (port) that the infrastructure implements.

```
┌──────────────────────────────────────────────────┐
│                   Interfaces                      │  HTTP, JSON (Spring MVC)
│         CarController · PublicCarController       │
│         AuthController · GlobalExceptionHandler   │
└───────────────────────┬──────────────────────────┘
                        │ calls use-case interfaces (port/in)
┌───────────────────────▼──────────────────────────┐
│                  Application                      │  Use case orchestration
│   CreateCarService · GetCarsService · AuthService │
│   Commands · CarDto                               │
└─────────┬──────────────────────┬─────────────────┘
          │ domain model         │ calls port/out interfaces
┌─────────▼──────────┐  ┌───────▼────────────────────┐
│       Domain        │  │      Infrastructure         │  Spring Data JPA,
│  Car · CarStatus    │  │  CarRepositoryAdapter       │  JWT, BCrypt,
│  AdminAccount       │  │  JwtTokenProvider           │  Flyway
│  Exceptions         │  │  SecurityConfig             │
│  Port interfaces    │  │  OpenApiConfig              │
└─────────────────────┘  └────────────────────────────┘
```

---

## Layers

### Domain (`domain/`)

**Rules: ZERO Spring or Jakarta imports. Pure Java only.**

| Package | Contents |
|---------|----------|
| `model/` | `Car`, `CarStatus`, `AdminAccount`, `TokenPair`, `PageResult<T>` |
| `port/in/` | `AuthUseCase` — interface implemented by `AuthService` |
| `port/out/` | `CarRepository`, `AdminAccountRepository`, `TokenPort`, `PasswordHashPort` — interfaces implemented by adapters |
| `exception/` | `CarNotFoundException`, `InvalidCarStateException`, `CarAlreadyExistsException`, `InvalidCredentialsException` |

Key decisions:
- `Car` has two factory methods: `Car.create()` (validates invariants) and `Car.reconstitute()` (DB → domain, no validation).
- Invariants are enforced in `Car` itself: daily price > 0, year in [1980, currentYear+1], currency must be a valid ISO 4217 code.
- `PageResult<T>` is a plain Java generic record — no Spring types — so it can be returned from `CarRepository` (domain port).

### Application (`application/`)

**Rules: may import domain. May use Spring annotations (`@Service`, `@Transactional`). No HTTP or persistence imports.**

| Package | Contents |
|---------|----------|
| `command/` | `CreateCarCommand`, `UpdateCarPriceCommand` — immutable input records for write use cases |
| `dto/` | `CarDto` — the single read projection returned by all use cases |
| `port/in/` | `CreateCarUseCase`, `DeleteCarUseCase`, `UpdateCarPriceUseCase`, `GetCarsUseCase`, `GetCarByIdUseCase` |
| `service/` | One `@Service` per use case, plus `AuthService` |

Use case interfaces are in `application/port/in/` (not domain) because they reference `CarDto`, which itself lives in the application layer.

`GetCarsUseCase` exposes three methods:
```java
List<CarDto> findAll();
List<CarDto> findByStatus(CarStatus status);
PageResult<CarDto> findPaged(int page, int size, CarStatus status);  // status null = all
```

### Infrastructure (`infrastructure/`)

**Rules: implements domain ports. Owns all Spring/JPA/JWT dependencies.**

| Package | Contents |
|---------|----------|
| `persistence/entity/` | `CarJpaEntity`, `AdminAccountJpaEntity` — JPA-annotated, kept separate from domain model |
| `persistence/repository/` | Spring Data `JpaRepository` interfaces |
| `persistence/adapter/` | `CarRepositoryAdapter`, `AdminRepositoryAdapter` — implement domain ports |
| `persistence/mapper/` | `CarMapper`, `AdminMapper` — static methods only, no Spring bean |
| `security/` | `SecurityConfig`, `JwtTokenProvider`, `JwtAuthenticationFilter`, `UserDetailsServiceImpl`, `PasswordHashAdapter` |
| `config/` | `OpenApiConfig` (Swagger bearer auth scheme), `AdminSeeder` (seeds default admin on startup) |

Security wiring details:
- `@Lazy` on `PasswordEncoder` in `UserDetailsServiceImpl` breaks the circular dependency with `SecurityConfig`.
- `AuthenticationManager` is injected via `@Bean` method parameter (not constructor) for the same reason.
- Spring Security exceptions (401, 403) bypass `@RestControllerAdvice`. They are handled by a custom `AuthenticationEntryPoint` and `AccessDeniedHandler` configured in `SecurityConfig`, which serialize the same `ApiError` JSON format.

### Interfaces (`interfaces/`)

**Rules: depends on application (use cases, DTOs). Translates HTTP ↔ application.**

| Package | Contents |
|---------|----------|
| `rest/` | `CarController` (admin), `PublicCarController` (public), `AuthController` |
| `dto/request/` | `CreateCarRequest`, `UpdateCarPriceRequest`, `LoginRequest`, `RefreshTokenRequest` — Jakarta validation annotations |
| `dto/response/` | `AuthResponse`, `ApiError` |
| `exception/` | `GlobalExceptionHandler` — `@RestControllerAdvice` |

Controllers inject **use case interfaces**, not services. This keeps the HTTP layer decoupled from any specific implementation.

---

## Dependency rule

```
interfaces  →  application  →  domain
infrastructure  →  domain  (implements ports)
infrastructure  →  application  (implements use cases, injects services)
```

**Domain has zero outward dependencies.** It never imports from `application`, `infrastructure`, or `interfaces`.

---

## Error handling

All errors are returned in the same JSON envelope (see [`API.md`](API.md#error-format)):

```
GlobalExceptionHandler  →  domain exceptions, validation, Spring MVC exceptions
SecurityConfig          →  401 (no/invalid token), 403 (insufficient role)
```

HTTP status mapping:

| Situation | Status |
|-----------|--------|
| Login failure / invalid token | 401 |
| Insufficient role | 403 |
| Resource not found | 404 |
| Method not allowed | 405 |
| Car already exists | 409 |
| Domain invariant violated | 422 |
| Malformed JSON / bad param type | 400 |
| Missing required param | 400 |
| Unexpected error | 500 |

---

## Authentication flow

```
Client                      API                          DB
  │                          │                            │
  │  POST /api/auth/login     │                            │
  │ ─────────────────────────►│                            │
  │                          │── findByUsername() ────────►│
  │                          │◄── AdminAccount ────────────│
  │                          │── BCrypt.verify() ──────────│
  │                          │── generateAccessToken()     │
  │                          │── generateRefreshToken()    │
  │◄── {accessToken, ...} ───│                            │
  │                          │                            │
  │  GET /api/admin/cars      │                            │
  │  Authorization: Bearer …  │                            │
  │ ─────────────────────────►│                            │
  │                          │─ JwtAuthenticationFilter    │
  │                          │  validates token type +     │
  │                          │  expiry, sets SecurityCtx   │
  │◄── 200 [CarDto, …] ──────│                            │
```

---

## Profiles

| Profile | Database | Swagger | Log level |
|---------|----------|---------|-----------|
| `dev` (default) | H2 in-memory | Enabled | DEBUG |
| `prod` | PostgreSQL | Disabled | WARN/INFO |
| `test` | H2 in-memory | — | — |

The `test` profile is activated via `@ActiveProfiles("test")` on integration tests. It uses a dedicated `src/test/resources/application-test.yml`.
