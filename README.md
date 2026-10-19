# Hospital Management System

A Java 21 and Spring Boot backend project.

## Current Phase: Foundation

- REST health endpoint: GET /api/v1/health
- PostgreSQL database connectivity configuration
- Flyway database migration for the hms schema
- Automated health-controller test

## Technology Stack

- Java 21
- Spring Boot 4.1.1
- Maven Wrapper
- PostgreSQL
- Flyway
- JUnit and Spring MVC testing

## Run Tests

```powershell
.\mvnw.cmd clean verify
```

## Database Configuration

The application expects a PostgreSQL database named hospital_management_db on localhost:5432.
Configure the DB_PASSWORD environment variable before starting the application. Never commit passwords or secrets.

## Planned Features

Authentication, authorization, patient management, doctor management, and appointment management are planned for future phases and are not yet claimed as implemented.
