# Hospital Management System

A resume-focused Java backend project built with Java 21, Spring Boot 4.1.1, PostgreSQL, and Maven.

The project is organized as a **multi-module Maven repository** with separate applications for authentication and clinical API access.

## Project Architecture

```text
hospital-management-system/
|-- pom.xml
|-- resource-server/
|   |-- pom.xml
|   |-- src/
|       |-- main/
|       |-- test/
|-- authorization-server/
    |-- pom.xml
```

### Resource Server

The `resource-server` module contains the existing Hospital Management System application.

Current responsibilities:
- REST API health endpoint: `GET /api/v1/health`
- PostgreSQL connection configuration
- Flyway database migration configuration
- Automated application and controller tests

Planned responsibilities:
- JWT validation and security configuration
- Role-based and method-level authorization
- Clinical APIs for departments, doctors, patients, appointments, medical records, prescriptions, and billing

### Authorization Server

The `authorization-server` module is a separate application boundary for the planned authentication and token-issuing functionality.

Planned responsibilities:
- OAuth 2.0 and OpenID Connect (OIDC)
- User identity and login
- Access-token issuance
- RSA-based signing keys and public-key discovery through JWKS
- Signing-key rotation
- OAuth 2.0 client configuration

**Status:** The module structure exists, but authentication and token-issuing functionality have not been implemented yet.

## Technology Stack

- Java 21
- Spring Boot 4.1.1
- Maven Wrapper
- PostgreSQL 18
- Flyway
- JUnit and Spring MVC testing

Additional technologies will be introduced as their implementation phases begin.

## Current Project Status

**Completed foundation:**
- Multi-module Maven structure
- Existing application moved into `resource-server`
- Separate `authorization-server` module created
- PostgreSQL connection configuration using dedicated database accounts
- Initial Flyway migration for the `hms` schema
- Health endpoint and automated tests
- Successful Maven verification after restructuring

**Not yet implemented:**
- OAuth 2.0/OIDC authentication
- JWT validation and issuance
- RSA/JWKS key management and rotation
- Clinical database tables and business APIs
- Role-based access control
- Docker and CI/CD

## Run Tests

From the project root, run the full Maven reactor build:

```powershell
.\mvnw.cmd clean verify
```

This verifies the modules and runs the tests configured in the project.

## Database Configuration

The application expects a PostgreSQL database named `hospital_management_db` on `localhost:5432`.

Configure these environment variables before running the application:

- `HMS_APP_PASSWORD`: password for the `hms_app` runtime database account.
- `HMS_MIGRATOR_PASSWORD`: password for the `hms_migrator` Flyway migration account.

The application uses separate accounts for runtime database access and migrations. Keep their credentials separate and never commit passwords or other secrets to Git.

The initial migration creates the `hms` schema. Clinical tables and related migrations will be introduced in future phases.

## Health Endpoint

After starting the resource-server application, send a request:

```powershell
Invoke-RestMethod http://localhost:8080/api/v1/health
```

The endpoint should return the application status and application name.

## Development Principles

- Keep authentication and clinical API responsibilities separated.
- Preserve least-privilege database access.
- Use version-controlled Flyway migrations for schema changes.
- Add automated tests as features are implemented.
- Document features as implemented only after they have been built and verified.
