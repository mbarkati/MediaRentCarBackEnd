# API Reference

Base URL: `http://localhost:8080`

All responses are JSON (`Content-Type: application/json`).
Protected routes require `Authorization: Bearer <accessToken>`.

---

## Table of contents

- [Error format](#error-format)
- [Auth](#auth)
  - [POST /api/auth/login](#post-apiauthlogin)
  - [POST /api/auth/refresh](#post-apiauthrefresh)
- [Public cars](#public-cars)
  - [GET /api/cars](#get-apicars)
  - [GET /api/cars/{id}](#get-apicarsid)
- [Admin cars](#admin-cars)
  - [POST /api/admin/cars](#post-apiadmincars)
  - [PATCH /api/admin/cars/{id}/price](#patch-apiadmincarsidprice)
  - [DELETE /api/admin/cars/{id}](#delete-apiadmincarsid)

---

## Error format

Every error — validation, domain, auth, or infrastructure — follows the same structure:

```json
{
  "timestamp": "2026-02-25T20:34:27.794Z",
  "status": 422,
  "error": "UNPROCESSABLE_ENTITY",
  "message": "Daily price must be greater than 0",
  "path": "/api/admin/cars"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `timestamp` | ISO-8601 UTC string | When the error occurred |
| `status` | integer | HTTP status code |
| `error` | string | HTTP status name |
| `message` | string | Human-readable description |
| `path` | string | Request URI that triggered the error |

### Common error codes

| Status | Situation |
|--------|-----------|
| 400 | Malformed JSON, validation failure, wrong param type |
| 401 | Missing/invalid/expired token, wrong credentials |
| 403 | Valid token but insufficient role |
| 404 | Car not found, unknown route |
| 405 | Wrong HTTP method |
| 409 | Car already exists |
| 422 | Domain invariant violated (price ≤ 0, invalid year, bad currency) |
| 500 | Unexpected server error |

---

## Auth

### POST /api/auth/login

Authenticate with admin credentials and receive a JWT token pair.

**Request**

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}
```

| Field | Type | Constraints |
|-------|------|-------------|
| `username` | string | required, not blank |
| `password` | string | required, not blank |

**Response — 200 OK**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInR5cGUiOiJBQ0NFU1MiLCJpYXQiOjE3NDA0OTU2NjcsImV4cCI6MTc0MDQ5OTI2N30.xxx",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInR5cGUiOiJSRUZSRVNIIiwiaWF0IjoxNzQwNDk1NjY3LCJleHAiOjE3NDExMDA0Njd9.yyy",
  "tokenType": "Bearer"
}
```

**Error cases**

```json
// 400 — missing field
{
  "timestamp": "2026-02-25T20:34:27Z",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "username: must not be blank",
  "path": "/api/auth/login"
}

// 401 — wrong credentials
{
  "timestamp": "2026-02-25T20:34:28Z",
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

---

### POST /api/auth/refresh

Exchange a valid refresh token for a new token pair.

**Request**

```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response — 200 OK**

Same structure as login.

**Error — 401** if the refresh token is expired or invalid.

---

## Public cars

No authentication required.

---

### GET /api/cars

List cars with optional status filter and pagination.

**Query parameters**

| Parameter | Type | Default | Constraints | Description |
|-----------|------|---------|-------------|-------------|
| `status` | enum | — | `AVAILABLE` or `UNAVAILABLE` | Filter by status. Omit for all cars. |
| `page` | integer | `0` | ≥ 0 | Zero-based page index |
| `size` | integer | `20` | 1–100 | Number of items per page |

**Examples**

```bash
# All cars, first page
curl http://localhost:8080/api/cars

# Only available cars, page 1, 5 per page
curl "http://localhost:8080/api/cars?status=AVAILABLE&page=1&size=5"
```

**Response — 200 OK**

```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "brand": "Renault",
      "model": "Clio",
      "year": 2022,
      "dailyPrice": 45.00,
      "currency": "EUR",
      "status": "AVAILABLE",
      "createdAt": "2026-02-25T10:00:00",
      "updatedAt": "2026-02-25T10:00:00"
    }
  ],
  "totalElements": 42,
  "totalPages": 9,
  "currentPage": 0,
  "first": true,
  "last": false
}
```

| Field | Description |
|-------|-------------|
| `content` | Array of `CarDto` objects for this page |
| `totalElements` | Total number of matching cars across all pages |
| `totalPages` | Total number of pages |
| `currentPage` | Zero-based index of the current page |
| `first` | `true` if this is the first page |
| `last` | `true` if this is the last page |

**Error — 400** if `page` < 0 or `size` outside [1, 100]:

```json
{
  "timestamp": "2026-02-25T20:34:27Z",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "size: must be less than or equal to 100",
  "path": "/api/cars"
}
```

---

### GET /api/cars/{id}

Retrieve a single car by its UUID.

**Path parameter**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | UUID | Car identifier |

**Example**

```bash
curl http://localhost:8080/api/cars/550e8400-e29b-41d4-a716-446655440000
```

**Response — 200 OK**

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "brand": "Renault",
  "model": "Clio",
  "year": 2022,
  "dailyPrice": 45.00,
  "currency": "EUR",
  "status": "AVAILABLE",
  "createdAt": "2026-02-25T10:00:00",
  "updatedAt": "2026-02-25T10:00:00"
}
```

**Errors**

```json
// 400 — malformed UUID
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Invalid value 'not-a-uuid' for parameter 'id'",
  "path": "/api/cars/not-a-uuid"
}

// 404 — car not found
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Car not found with id: 550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/cars/550e8400-e29b-41d4-a716-446655440000"
}
```

---

## Admin cars

All routes require `Authorization: Bearer <accessToken>` with the `ADMIN` role.

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}' | jq -r .accessToken)
```

---

### POST /api/admin/cars

Add a car to the fleet.

**Request**

```http
POST /api/admin/cars
Content-Type: application/json
Authorization: Bearer <token>

{
  "brand": "Peugeot",
  "model": "308",
  "year": 2023,
  "dailyPrice": 55.00,
  "currency": "EUR"
}
```

| Field | Type | Constraints |
|-------|------|-------------|
| `brand` | string | required, not blank |
| `model` | string | required, not blank |
| `year` | integer | required, ≥ 1980 |
| `dailyPrice` | decimal | required, > 0 |
| `currency` | string | required, exactly 3 characters (ISO 4217 code) |

**Response — 201 Created**

```
Location: /api/admin/cars/b5e6f7a8-...
```

```json
{
  "id": "b5e6f7a8-1234-5678-abcd-ef0123456789",
  "brand": "Peugeot",
  "model": "308",
  "year": 2023,
  "dailyPrice": 55.00,
  "currency": "EUR",
  "status": "AVAILABLE",
  "createdAt": "2026-02-25T20:00:00",
  "updatedAt": "2026-02-25T20:00:00"
}
```

New cars are always created with status `AVAILABLE`.

**Error cases**

```json
// 400 — validation failure
{
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "brand: must not be blank, dailyPrice: must be greater than 0",
  "path": "/api/admin/cars"
}

// 401 — missing or invalid token
{
  "status": 401,
  "error": "UNAUTHORIZED",
  "message": "Authentication required",
  "path": "/api/admin/cars"
}

// 403 — valid token but not ADMIN
{
  "status": 403,
  "error": "FORBIDDEN",
  "message": "Access denied",
  "path": "/api/admin/cars"
}

// 422 — domain invariant violated
{
  "status": 422,
  "error": "UNPROCESSABLE_ENTITY",
  "message": "Invalid ISO 4217 currency code: XYZ",
  "path": "/api/admin/cars"
}
```

**Full cURL example**

```bash
curl -s -X POST http://localhost:8080/api/admin/cars \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "brand": "Peugeot",
    "model": "308",
    "year": 2023,
    "dailyPrice": 55.00,
    "currency": "EUR"
  }' | jq
```

---

### PATCH /api/admin/cars/{id}/price

Update the daily rental price and/or currency of a car.

**Request**

```http
PATCH /api/admin/cars/b5e6f7a8-1234-5678-abcd-ef0123456789/price
Content-Type: application/json
Authorization: Bearer <token>

{
  "dailyPrice": 60.00,
  "currency": "EUR"
}
```

| Field | Type | Constraints |
|-------|------|-------------|
| `dailyPrice` | decimal | required, > 0 |
| `currency` | string | required, exactly 3 characters (ISO 4217) |

**Response — 200 OK**

Updated `CarDto` (same structure as POST response).

**Error — 422** if the new price ≤ 0 or the currency is not a valid ISO 4217 code.

**cURL example**

```bash
curl -s -X PATCH \
  http://localhost:8080/api/admin/cars/b5e6f7a8-1234-5678-abcd-ef0123456789/price \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"dailyPrice": 60.00, "currency": "EUR"}' | jq
```

---

### DELETE /api/admin/cars/{id}

Remove a car from the fleet permanently.

**Request**

```http
DELETE /api/admin/cars/b5e6f7a8-1234-5678-abcd-ef0123456789
Authorization: Bearer <token>
```

**Response — 204 No Content**

Empty body.

**Error — 404** if the car does not exist.

**cURL example**

```bash
curl -s -X DELETE \
  http://localhost:8080/api/admin/cars/b5e6f7a8-1234-5678-abcd-ef0123456789 \
  -H "Authorization: Bearer $TOKEN" \
  -w "%{http_code}"
# → 204
```

---

## Car object reference (`CarDto`)

| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `brand` | string | Manufacturer name (e.g. `"Renault"`) |
| `model` | string | Model name (e.g. `"Clio"`) |
| `year` | integer | Manufacturing year — range [1980, currentYear+1] |
| `dailyPrice` | decimal | Daily rental price (e.g. `45.00`) |
| `currency` | string | ISO 4217 currency code in uppercase (e.g. `"EUR"`) |
| `status` | enum | `AVAILABLE` or `UNAVAILABLE` |
| `createdAt` | ISO-8601 | Record creation timestamp |
| `updatedAt` | ISO-8601 | Last modification timestamp |
