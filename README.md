# job-application-tracker-backend

Spring Boot backend for tracking job applications.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Maven

## Requirements

- Java 17 or newer
- Maven
- PostgreSQL

## Database

Create a PostgreSQL database:

```sql
CREATE DATABASE job_tracker;
```

## Environment Variables

```text
DB_URL=jdbc:postgresql://localhost:5432/job_tracker
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=replace-with-a-long-secret
JWT_EXPIRATION_MINUTES=60
```

## Run

```bash
mvn spring-boot:run
```

The API runs on:

```text
http://localhost:8080
```

Swagger is available at:

```text
http://localhost:8080/swagger-ui.html
```

## API Base URL

```text
/api/v1
```

## Main Flow

Register a user, log in, copy the JWT token, then send protected requests with:

```text
Authorization: Bearer <token>
```
