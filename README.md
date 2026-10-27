# Hospital Management System

A Java 21 and Spring Boot backend project.

## Current Phase: Foundation

- REST health endpoint: `GET /api/v1/health`
- PostgreSQL database connectivity configuration
- Flyway database migration for the `hms` schema
- Automated health-controller test

## Technology Stack

- Java 21
- Spring Boot 4.1.1
- Maven Wrapper
- PostgreSQL
- Flyway
- JUnit and Spring MVC testing

## Run Tests

Run the following command from the project root directory using Windows PowerShell:

```powershell
.\mvnw.cmd clean verify
```

## Database Configuration

The application expects a PostgreSQL database named `hospital_management_db` on `localhost:5432`.

Configure these environment variables before starting the application:

- `HMS_APP_PASSWORD`: password for the application runtime database account (`hms_app`).
- `HMS_MIGRATOR_PASSWORD`: password for the Flyway migration database account (`hms_migrator`).

The application uses `hms_app` for runtime database access and `hms_migrator` for database migrations. Keep these accounts and their passwords separate.

Never commit passwords or other secrets to Git.

## Health Endpoint

When the application is running, check its health using:

```powershell
Invoke-RestMethod http://localhost:8080/api/v1/health
```

The endpoint should return a response containing the application status and name.

## Planned Features

Authentication, authorization, patient management, doctor management, and appointment management are planned for future phases and are not yet claimed as implemented.
