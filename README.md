# 🏥 MediConnect — Healthcare Management Platform

> A production-ready microservices backend built with **Java 17**, **Spring Boot 3**, **Spring Cloud**, and **MongoDB**.

---

## 📋 Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Microservices](#microservices)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Email Notifications Setup](#email-notifications-setup)
- [Deployment](#deployment)
- [Project Structure](#project-structure)

---

## Overview

MediConnect is a full-featured REST API backend for managing a healthcare clinic. It supports:

- 🔐 JWT-based authentication with role-based access (Patient, Doctor, Admin)
- 👤 Patient registration and medical profile management
- 👨‍⚕️ Doctor profiles, specializations, and availability slots
- 📅 Appointment booking with conflict detection
- 💊 Digital prescription management
- 📧 Automated email notifications for appointments

---

## Architecture

```
Client (Mobile / Web)
         │
         ▼
  ┌─────────────┐
  │ API Gateway │  :8080  — JWT auth, routing, rate limiting
  └──────┬──────┘
         │  routes via Eureka service discovery
   ┌─────┴──────────────────────────────────┐
   ▼         ▼           ▼          ▼       ▼
[Auth]   [Patient]  [Doctor]  [Appointment] [Prescription]  [Notification]
:8081     :8082      :8083      :8084         :8085            :8086
   │         │           │           │
   └─────────┴───────────┴───────────┘
                     │
              Eureka Server :8761
                     │
             Each service → own MongoDB database
```

---

## Microservices

| Service | Port | Database | Description |
|---|---|---|---|
| **Eureka Server** | 8761 | — | Service registry & discovery |
| **API Gateway** | 8080 | — | Single entry point, JWT validation |
| **Auth Service** | 8081 | mediconnect_auth | Register, login, JWT tokens |
| **Patient Service** | 8082 | mediconnect_patients | Patient profiles & medical history |
| **Doctor Service** | 8083 | mediconnect_doctors | Doctor profiles & availability |
| **Appointment Service** | 8084 | mediconnect_appointments | Booking & scheduling |
| **Prescription Service** | 8085 | mediconnect_prescriptions | Digital prescriptions |
| **Notification Service** | 8086 | mediconnect_notifications | Email alerts & reminders |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Cloud | Spring Cloud 2023 (Eureka, Gateway, Feign) |
| Database | MongoDB 7 |
| Security | Spring Security + JWT (JJWT 0.11.5) |
| Build Tool | Maven |
| Containerization | Docker + Docker Compose |
| Email | Spring Mail (Gmail SMTP / SendGrid) |
| Utilities | Lombok |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- MongoDB (or use the Docker container)

### Option A — Run with Docker (Recommended)

```bash
# 1. Clone the repo
git clone https://github.com/yourusername/mediconnect.git
cd mediconnect

# 2. Set up environment variables
cp .env.example .env
# Edit .env and add your Gmail credentials

# 3. Start everything
docker-compose up --build

# All services will be running in ~2-3 minutes
```

### Option B — Run Locally (Development)

```bash
# 1. Start MongoDB locally
mongod --dbpath /data/db

# 2. Start services in this ORDER:
#    Terminal 1:
cd eureka-server && mvn spring-boot:run

#    Terminal 2:
cd api-gateway && mvn spring-boot:run

#    Terminal 3, 4, 5, 6, 7, 8:
cd auth-service && mvn spring-boot:run
cd patient-service && mvn spring-boot:run
cd doctor-service && mvn spring-boot:run
cd appointment-service && mvn spring-boot:run
cd prescription-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

### Verify Everything is Running
- **Eureka Dashboard**: http://localhost:8761 — all services should appear here
- **API Gateway**: http://localhost:8080 — all requests go through here

---

## API Reference

All requests go through the API Gateway at `http://localhost:8080`.

### 🔐 Auth
```
POST /api/auth/register   — Register a new user
POST /api/auth/login      — Login and get JWT token
```

**Register example:**
```json
POST /api/auth/register
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "password": "password123",
  "role": "PATIENT"
}
```

**Login response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": "64a1b2c3d4e5f6789...",
  "name": "Rahul Sharma",
  "role": "PATIENT"
}
```

Use the token in all subsequent requests:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

### 👤 Patients
```
POST   /api/patients              — Create patient profile
GET    /api/patients/{id}         — Get patient by ID
GET    /api/patients/user/{userId}— Get patient by userId
GET    /api/patients              — Get all patients (admin)
GET    /api/patients/search?name= — Search patients by name
PUT    /api/patients/{id}         — Update patient
DELETE /api/patients/{id}         — Deactivate patient
```

---

### 👨‍⚕️ Doctors
```
POST   /api/doctors                        — Create doctor profile
GET    /api/doctors/{id}                   — Get doctor by ID
GET    /api/doctors                        — Get all doctors
GET    /api/doctors/specialization/{spec}  — Filter by specialization
GET    /api/doctors/search?name=           — Search by name
PUT    /api/doctors/{id}                   — Update doctor
```

---

### 📅 Appointments
```
POST   /api/appointments                        — Book appointment
GET    /api/appointments/{id}                   — Get appointment
GET    /api/appointments/patient/{patientId}    — Patient's appointments
GET    /api/appointments/doctor/{doctorId}      — Doctor's appointments
GET    /api/appointments/doctor/{id}/date/{date}— Doctor's schedule for a date
PUT    /api/appointments/{id}/status            — Update status
DELETE /api/appointments/{id}                   — Cancel appointment
```

**Book appointment example:**
```json
POST /api/appointments
{
  "patientId": "64a1...",
  "doctorId": "64b2...",
  "appointmentDate": "2024-02-15",
  "appointmentTime": "10:30",
  "reasonForVisit": "Fever and headache"
}
```

---

### 💊 Prescriptions
```
POST /api/prescriptions                         — Create prescription
GET  /api/prescriptions/{id}                    — Get prescription
GET  /api/prescriptions/appointment/{id}        — Get by appointment
GET  /api/prescriptions/patient/{patientId}     — Patient's prescriptions
GET  /api/prescriptions/doctor/{doctorId}       — Doctor's prescriptions
```

**Create prescription example:**
```json
POST /api/prescriptions
{
  "appointmentId": "64c3...",
  "patientId": "64a1...",
  "doctorId": "64b2...",
  "diagnosis": "Viral Fever",
  "medicines": [
    {
      "name": "Paracetamol 500mg",
      "dosage": "1 tablet",
      "frequency": "Twice daily",
      "duration": "5 days",
      "instructions": "After food"
    }
  ],
  "advice": "Rest for 3 days, drink plenty of fluids",
  "followUpDate": "2024-02-22"
}
```

---

### 🔔 Notifications
```
POST /api/notifications/send                    — Send custom email
POST /api/notifications/appointment-confirmation— Send booking confirmation
POST /api/notifications/appointment-reminder    — Send reminder
POST /api/notifications/welcome                 — Send welcome email
GET  /api/notifications/email/{email}           — Get notifications by email
GET  /api/notifications/failed                  — Get failed notifications (admin)
```

---

## Email Notifications Setup

1. Enable **2-Step Verification** on your Google account
2. Go to: **Google Account → Security → App Passwords**
3. Generate a new App Password for "Mail"
4. Add to your `.env` file:

```env
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=xxxx-xxxx-xxxx-xxxx
```

---

## Deployment (Free on Railway)

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login
railway login

# Create project and deploy
railway new
railway up

# Add MongoDB plugin from Railway dashboard
# Set environment variables in Railway dashboard
```

---

## Project Structure

```
mediconnect/
├── eureka-server/
├── api-gateway/
├── auth-service/
│   └── src/main/java/com/mediconnect/auth/
│       ├── controller/    AuthController.java
│       ├── service/       AuthService.java
│       ├── repository/    UserRepository.java
│       ├── model/         User.java
│       ├── dto/           AuthDto.java
│       ├── security/      JwtService.java
│       └── config/        SecurityConfig.java, GlobalExceptionHandler.java
├── patient-service/
├── doctor-service/
├── appointment-service/
├── prescription-service/
├── notification-service/
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

---

## 👨‍💻 Author

Built as a full-stack microservices project to demonstrate:
- Spring Boot 3 REST API development
- Microservices architecture with Spring Cloud
- MongoDB data modelling
- JWT authentication & role-based access
- Inter-service communication with Feign
- Docker containerisation
- Real-world healthcare domain implementation

---

## 📄 License

MIT License — free to use for learning and projects.
