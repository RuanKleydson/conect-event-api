# Conect API

RESTful authentication API built with Spring Boot. Developed to deepen knowledge in **Spring Security**, **OAuth2 Resource Server**, and **JWT with asymmetric encryption (RSA)**.

---

## 🚀 Technologies Used

* Java 21
* Spring Boot 3.x
* Spring Security + OAuth2 Resource Server
* JWT (RSA asymmetric encryption)
* Spring Data JPA / Hibernate
* MySQL
* Maven
* Docker & Docker Compose

---

## 🎯 Project Goal

This project was developed to consolidate concepts of **authentication**, **authorization with RBAC**, and **stateless token-based security** in Spring Boot, using production-ready patterns such as RSA-signed JWTs and BCrypt password hashing.

---

## 🔐 Authentication

Authentication uses RSA-signed JWT tokens. Register a user first, then log in to obtain a token.

### 🔸 User Registration

`POST /auth/register`

```json
{
  "email": "user@example.com",
  "password": "securepassword"
}
```

**Response:**

* `201 Created` — user created with `BASIC` role
* `400 Bad Request` — missing fields, invalid email, or password too short
* `409 Conflict` — email already registered

### 🔸 Login

`POST /auth/login`

```json
{
  "email": "user@example.com",
  "password": "securepassword"
}
```

**Response (200 OK):**

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiJ9...",
  "expires_in": 300
}
```

**Error responses:**

* `400 Bad Request` — missing fields or invalid email format
* `401 Unauthorized` — invalid email or password

> All protected endpoints require the `Authorization: Bearer <token>` header.

---

## 🔒 Security Highlights

* **RSA key pairs** — tokens are signed with a private key and verified with the public key (asymmetric)
* **BCrypt password hashing** — automatic salt generation
* **Stateless sessions** — no server-side session storage; every request carries a JWT
* **RBAC** — role-based access control (`ADMIN` / `BASIC`) via many-to-many relationship
* **Auto-provisioned admin** — an admin user is bootstrapped on first startup

---

## ⚙️ How to Run

### Prerequisites

* Java 21+
* Maven 3.8+
* Docker & Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/conectapi.git
cd conectapi
```

### 2. Generate RSA keys

```bash
openssl genrsa -out src/main/resources/private.key 2048
openssl rsa -in src/main/resources/private.key -pubout -out src/main/resources/public.key
```

> Keys are ignored by `.gitignore` and must be generated locally.

### 3. Start the database

```bash
docker compose -f docker/docker-compose.yml up -d
```

### 4. Run the application

```bash
mvn spring-boot:run
```

API available at `http://localhost:8080`.

### 5. Test the API

```bash
# Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"mypassword123"}'

# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"mypassword123"}'

# Use token to access a protected endpoint
curl -X GET http://localhost:8080/protected \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Stop

```bash
docker compose -f docker/docker-compose.yml down
```

---

## 🗂️ Project Structure

```
src/main/java/dev/ruan/conectapi/
├── config/           # Security config & admin bootstrap
├── controller/       # REST endpoints (Auth, User)
│   └── dto/          # Request/Response records
├── entities/         # JPA entities (User, Role)
├── repository/       # Spring Data repositories
└── ConectapiApplication.java
```

---

## ✍️ Author

**Ruan Kleydson** — Backend Developer | Java & Spring Boot

> © 2026 Ruan Kleydson. Licensed under the MIT License.
