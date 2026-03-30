# Multi-Tenant Healthcare Backend

> **Want to test the APIs right away? Jump to → [Quick Start & Testing](#quick-start--testing)**

---

## Table of Contents

- [What Is This Project?](#what-is-this-project)
- [What Was Asked vs What Was Built](#what-was-asked-vs-what-was-built)
- [System Overview](#system-overview)
- [Tech Stack](#tech-stack)
- [Quick Start & Testing](#quick-start--testing)
- [How to Test the APIs](#how-to-test-the-apis)
- [Role Guide](#role-guide)
- [Authentication Flow](#authentication-flow)
- [Complete API Reference](#complete-api-reference)
- [Frontend UI](#frontend-ui)
- [Database Schema](#database-schema)
- [Scalability Notes](#scalability-notes)
- [Project Structure](#project-structure)
- [Environment Variables](#environment-variables)

---

## What Is This Project?

This is a **production-grade REST API** for a multi-tenant hospital management system — built to demonstrate secure, scalable backend engineering.

**Don't worry if you're not a Java/Spring Boot developer.** This guide is written for everyone. You don't need to understand the code — just follow the steps and you'll have the entire system running and testable in minutes through a clean browser UI.

---

## What Was Asked vs What Was Built

| Requirement | Status | What's Implemented |
|---|---|---|
| User Registration & Login | Exceeded | JWT access tokens + refresh token rotation |
| Role-Based Access Control | Exceeded | 5 roles: PATIENT, DOCTOR, HOSPITAL_ADMIN, ADMIN, USER |
| CRUD APIs for a secondary entity | Exceeded | Full CRUD across 7 modules |
| API Versioning & Error Handling | Done | Global exception handler, structured error responses |
| Input Validation | Done | Bean Validation on all request bodies |
| API Documentation | Done | Interactive Swagger UI — test every endpoint in the browser |
| Database Schema | Done | MySQL with normalized relational schema |
| Security | Done | BCrypt hashing, JWT, idempotency keys, stateless auth |
| Caching (Optional) | Done | Inmemory caching on read-heavy endpoints |
| Docker (Optional) | Done | Full Docker + Docker Compose setup |
| Frontend UI | Done | React.js frontend (see [Frontend UI](#frontend-ui)) |

---

## System Overview

This API manages the full lifecycle of a hospital ecosystem:

```
Hospital Admin  →  Creates hospitals, onboards doctors, manages specializations
Doctor          →  Sets availability, manages appointment slots, creates medical records
Patient         →  Books appointments, views medical history, processes payments
```

### Modules Built

- **Authentication** — Register, login, token refresh, logout
- **Patient Management** — Patient profiles, self-service updates
- **Doctor Management** — Onboarding, availability scheduling
- **Appointment Management** — Booking with idempotency, cancellation
- **Hospital Management** — Multi-hospital support
- **Appointment Slots** — Doctor availability windows
- **Specialization Management** — Medical specializations (soft delete)
- **Medical Records** — Per-appointment clinical records
- **Payment Management** — Appointment payment processing

---

## Tech Stack

> You don't need to know any of this to run the project — it's just for reference.

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.1 |
| Database | MySQL 8 |
| Authentication | JWT (Access + Refresh Tokens) |
| Caching | Inmemory cache |
| API Docs | Swagger / OpenAPI 3 |
| Build Tool | Maven |
| Containerization | Docker + Docker Compose |

---

## Quick Start & Testing

There are two ways to run this project. **Option A (Docker) is strongly recommended** — it requires no Java or Maven knowledge at all.

---

### Option A — Docker (Recommended)

**Prerequisites:** Just [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed on your machine.

**Step 1 — Clone the repository**
```bash
git clone https://github.com/Alok1764/multi-tenant-healthcare-backend.git
cd multi-tenant-healthcare-backend
```

**Step 2 — Start everything with one command**
```bash
docker-compose up --build
```

This automatically starts:
- The Spring Boot API on `http://localhost:8080`
- MySQL database on port `3306`

> **Note:** If you have MySQL installed locally, make sure to stop it first before running Docker — otherwise port 3306 will conflict. On Windows: `Services → MySQL → Stop`. On Mac: `System Preferences → MySQL → Stop`.

Wait about 30 seconds for everything to initialize. You'll see `Started HealthcareApplication` in the logs when it's ready.

**Step 3 — Open the app**
This automatically starts:
- The React Client on `http://localhost:5173`

**Step 4 — Open Swagger UI**

Go to: **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

You're ready to test.

**To stop:**
```bash
docker compose down
```

---

### Option B — Run Locally (Without Docker)

**Prerequisites:**
- [Java 17+](https://adoptium.net/) — download and install
- [Maven 3.8+](https://maven.apache.org/download.cgi) — download and install
- [MySQL 8](https://dev.mysql.com/downloads/mysql/) — running locally

**Step 1 — Clone the repository**
```bash
git clone https://github.com/Alok1764/multi-tenant-healthcare-backend.git
cd multi-tenant-healthcare-backend
```

**Step 2 — Create the database**

Open MySQL and run:
```sql
CREATE DATABASE healthcare_system;
```

**Step 3 — Configure the application**

Open `backend/src/main/resources/application.properties` and update these lines:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/healthcare_system
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

**Step 4 — Run the application**
```bash
cd backend
./mvnw spring-boot:run
```

Wait for `Started HealthcareApplication` in the console.

**Step 5 — Start the frontend**

Open a new terminal and run:
```bash
cd frontend
npm install
npm run dev
```
Open **http://localhost:5173** in your browser.

**Step 6 — Open Swagger UI**

Go to: **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

---

## How to Test the APIs

Everything is testable through the **Swagger UI** in your browser — no Postman, no curl, no code needed.

### Step 1 — Register a user

In Swagger UI:
1. Find the **Authentication** section
2. Click `POST /api/auth/register`
3. Click **Try it out**
4. Paste this into the request body:
```json
{
  "fullName": "Test User",
  "email": "testuser@example.com",
  "password": "Password@123",
  "role": "ROLE_PATIENT"
}
```
5. Click **Execute**
6. You'll get back an `accessToken` and a `refreshToken`

### Step 2 — Authorize in Swagger

1. Copy the `accessToken` from the response
2. Click the **Authorize** button at the top right of the Swagger page
3. Paste the token in the field (the `Bearer ` prefix is added automatically)
4. Click **Authorize** then **Close**

All protected endpoints are now unlocked for your session.

### Step 3 — Test any endpoint

Every endpoint shows exactly:
- What it does
- What roles can access it
- What the request body should look like
- What response codes to expect

---

## Role Guide

| Role | What They Can Do |
|---|---|
| `ROLE_PATIENT` | Book appointments, view own profile, view medical records, make payments |
| `ROLE_DOCTOR` | Manage availability, view appointments, create medical records |
| `ROLE_HOSPITAL_ADMIN` | Onboard doctors, create hospitals, manage specializations, add patients |
| `ROLE_ADMIN` | View medical records, system-wide access |

To test a specific role, register with that role string in the `role` field during registration.

---

## Authentication Flow

```
1. POST /api/auth/register   →  Get accessToken + refreshToken
2. Use accessToken in Authorization header: Bearer <token>
3. When accessToken expires → POST /api/auth/refresh-token with refreshToken
4. POST /api/auth/logout     →  Invalidates refresh token server-side
```

Access tokens are **short-lived** (15 minutes by default). Refresh tokens are **long-lived** (7 days). This is industry-standard token rotation — the same pattern used by Google, GitHub, and most modern APIs.

---

## Complete API Reference

| Module | Endpoint | Method | Role Required |
|---|---|---|---|
| **Auth** | `/api/auth/register` | POST | Public |
| | `/api/auth/login` | POST | Public |
| | `/api/auth/refresh-token` | POST | Public |
| | `/api/auth/logout` | POST | Public |
| **Patient** | `/api/patients` | POST | HOSPITAL_ADMIN |
| | `/api/patients/me` | GET | PATIENT |
| | `/api/patients/profile` | PUT | PATIENT |
| **Doctor** | `/api/doctors` | POST | HOSPITAL_ADMIN |
| | `/api/doctors` | GET | Public |
| | `/api/doctors/{id}` | GET | Public |
| | `/api/doctors/availability` | POST | DOCTOR |
| **Appointment** | `/api/appointments/book` | POST | PATIENT |
| | `/api/appointments/{id}` | GET | Authenticated |
| | `/api/appointments/patient/{patientId}` | GET | PATIENT, DOCTOR |
| | `/api/appointments/doctor/{doctorId}` | GET | DOCTOR |
| | `/api/appointments/{id}/cancel` | POST | PATIENT, DOCTOR |
| **Hospital** | `/api/hospitals` | POST | HOSPITAL_ADMIN |
| | `/api/hospitals/{id}` | GET | Public |
| | `/api/hospitals` | GET | Public |
| **Appointment Slots** | `/api/appointment-slots` | POST | DOCTOR, HOSPITAL_ADMIN |
| **Specialization** | `/api/specializations` | POST | HOSPITAL_ADMIN |
| | `/api/specializations/{id}` | GET | Public |
| | `/api/specializations` | GET | Public |
| | `/api/specializations/{id}` | PUT | HOSPITAL_ADMIN |
| | `/api/specializations/{id}` | DELETE | HOSPITAL_ADMIN |
| **Medical Records** | `/api/medical-records` | POST | DOCTOR |
| | `/api/medical-records/{id}` | GET | DOCTOR, PATIENT, ADMIN |
| | `/api/medical-records/patient/{patientId}` | GET | DOCTOR, PATIENT, ADMIN |
| **Payment** | `/api/payments/pay` | POST | PATIENT |
| | `/api/payments/appointment/{appointmentId}` | GET | PATIENT, ADMIN |

---

## Frontend UI

A lightweight React.js frontend is included and runs automatically with Docker.

**Screens:**
- Landing page — system overview and feature highlights
- Login & Register — with role selection
- Patient Dashboard — view profile, appointments, medical records, doctors
- Doctor Dashboard — manage availability, view appointments
- Admin Panel — manage hospitals, doctors, specializations, patients

Open **[http://localhost:5173](http://localhost:5173)** after running `docker compose up --build`.

**Demo credentials (password for all: `Password@123`)**

| Role | Email |
|---|---|
| Hospital Admin | admin@healthcare.com |
| Doctor | arjun@healthcare.com |
| Patient | rahul@healthcare.com |

---

## Database Schema

The schema is automatically created by the application on first run (via Hibernate auto-DDL). Key relationships:

```
User (base)
  ├── Patient (extends User)
  └── Doctor  (extends User, has Specialization)

Hospital
  └── has many Doctors

Doctor
  └── has Availability → generates AppointmentSlots

AppointmentSlot
  └── booked into Appointment

Appointment
  ├── has MedicalRecord
  └── has Payment
```

---

## Scalability Notes

This project is built with production scalability in mind, not just as a demo:

**Stateless Authentication** — JWT means any number of server instances can validate tokens without shared session state. Horizontal scaling works out of the box.

**Spring Cache** — Read-heavy endpoints (doctor listings, hospital listings, specializations) 
are cached using Spring's built-in cache, reducing database load significantly as traffic grows.

**Idempotency Keys** — The appointment booking endpoint requires an `X-Idempotency-Key` header. This prevents duplicate bookings if a client retries a failed request — a critical pattern for payment and booking systems at scale.

**Soft Deletes** — Specializations and other entities use soft deletes (deactivation) rather than hard deletes, preserving referential integrity and audit history.

**Modular Structure** — Every feature is a self-contained module (controller → service → repository). Adding a new feature doesn't touch existing code. This structure maps directly to a microservices split if the system needs to scale further.

**Docker Ready** — The entire stack (API + MySQL) runs in containers, making deployment 
to any cloud provider (AWS, GCP, Azure) straightforward.

---

## Project Structure
```
multi-tenant-healthcare-backend/
├── backend/                        # Spring Boot application
│   ├── src/main/java/com/healthcare/
│   │   ├── controller/             # API endpoints
│   │   ├── service/                # Business logic
│   │   ├── repository/             # Database queries
│   │   ├── dto/                    # Request & response shapes
│   │   ├── model/                  # Database entities
│   │   ├── security/               # JWT, filters
│   │   ├── exception/              # Global error handling
│   │   ├── config/                 # Spring configuration
│   │   └── swagger/                # API documentation
│   └── src/main/resources/
│       └── application.properties
├── frontend/                       # React.js application
│   ├── src/
│   │   ├── pages/                  # Landing, Auth, Dashboard
│   │   ├── components/             # Sidebar, Toast, Forms
│   │   └── api/                    # API client
│   └── package.json
├── docker-compose.yml              # Orchestrates full stack
└── README.md
```

---

## Environment Variables

If running with Docker, these are pre-configured in `docker-compose.yml`. For local setup, set them in `application.properties`:

| Variable | Description | Default |
|---|---|---|
| `DB_URL` | MySQL connection URL | `localhost:3306/healthcare_db` |
| `DB_USERNAME` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | — |
| `JWT_SECRET` | Secret key for signing JWT tokens | — |
| `JWT_EXPIRY_MS` | Access token expiry in milliseconds | `900000` (15 min) |
| `REFRESH_TOKEN_EXPIRY_DAYS` | Refresh token expiry | `7` |

---

## Questions?

If anything doesn't work or you need a specific scenario tested, the Swagger UI has full documentation on every request and response. All edge cases (invalid tokens, wrong roles, duplicate bookings) return structured JSON error responses with clear messages.
