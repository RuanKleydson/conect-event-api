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

It also features a **document control and submission system**, allowing authenticated users to download a template, fill it out, upload it, and have it reviewed by an admin.

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

## 📄 Document Control

Endpoints for managing document submissions. A template is provided for users to fill out and upload.

### 🔹 Download Template

`GET /documents/template`

Requires: `ROLE_BASIC` or `ROLE_ADMIN`

Downloads the `modelo.docx` template file.

### 🔹 Submit Document

`POST /documents/submissions`

Requires: `ROLE_BASIC`

Upload the filled template as a multipart file.

```bash
curl -X POST http://localhost:8080/documents/submissions \
  -H "Authorization: Bearer <TOKEN>" \
  -F "file=@modelo_preenchido.docx;type=application/vnd.openxmlformats-officedocument.wordprocessingml.document"
```

**Response (201 Created):**

```json
{
  "id": 1,
  "fileName": "modelo_preenchido.docx",
  "filePath": "uploads/<userId>/modelo_preenchido.docx",
  "status": "PENDING",
  "submissionDate": "2026-05-13T12:00:00Z"
}
```

### 🔹 Approve Submission

`PUT /documents/submissions/{id}/approve`

Requires: `ROLE_ADMIN`

Approves a pending submission by ID.

```bash
curl -X PUT http://localhost:8080/documents/submissions/1/approve \
  -H "Authorization: Bearer <ADMIN_TOKEN>"
```

**Response:** `200 OK` (empty body)

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

# Download template (replace YOUR_TOKEN)
curl -X GET http://localhost:8080/documents/template \
  -H "Authorization: Bearer YOUR_TOKEN" \
  --output modelo.docx

# Submit filled document
curl -X POST http://localhost:8080/documents/submissions \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@modelo.docx;type=application/vnd.openxmlformats-officedocument.wordprocessingml.document"

# Approve submission (requires ADMIN token)
curl -X PUT http://localhost:8080/documents/submissions/1/approve \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Stop

```bash
docker compose -f docker/docker-compose.yml down
```

---

## 🗂️ Project Structure

```
src/main/java/dev/ruan/conectapi/
├── config/                 # Security config & admin bootstrap
│   ├── AdminUserConfig.java
│   ├── RsaKeysConfig.java
│   └── SecurityConfig.java
├── controller/             # REST endpoints
│   ├── DocumentController.java   # Template download & submission upload
│   ├── SubmissionController.java # Submission approval
│   ├── TokenController.java      # JWT generation (login)
│   ├── UserController.java       # User registration
│   └── dto/                      # Request/Response records
│       ├── CreateSubmissionDto.java
│       ├── CreateUserDto.java
│       ├── LoginRequest.java
│       └── LoginResponse.java
├── entities/               # JPA entities
│   ├── Role.java
│   ├── Submission.java
│   └── User.java
├── repository/             # Spring Data repositories
│   ├── RoleRepository.java
│   ├── SubmissionRepository.java
│   └── UserRepository.java
└── ConectapiApplication.java
```

---

## ✍️ Author

**Ruan Kleydson** — Backend Developer | Java & Spring Boot

> © 2026 Ruan Kleydson. Licensed under the MIT License.
