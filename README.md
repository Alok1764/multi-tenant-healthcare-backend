# Healthcare Application

A Spring Boot 4.0.0 application with PostgreSQL, JWT authentication, and comprehensive API documentation.

## Technologies Used

- **Spring Boot 4.0.0** (Java 17)
- **Spring Web** - RESTful API
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database ORM
- **MySQL** - Database
- **Lombok** - Boilerplate code reduction
- **Bean Validation** - Input validation
- **JWT (JJWT 0.12.3)** - Token-based authentication

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL

## Database Setup

1. Install MySQL
2. Create a database:
```sql
CREATE DATABASE healthcare_system;
```

## Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, access the Swagger UI at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## Project Structure

```
src/main/java/com/healthcare/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── service/         # Business logic
├── repository/      # Data access layer
├── model/           # JPA entities
├── dto/             # Data Transfer Objects
├── security/        # JWT & security components
└── exception/       # Custom exceptions

src/main/resources/
├── application.properties  # Application configuration

```

## Database Schema

The application includes a comprehensive database schema with 15 tables supporting multi-tenant architecture. See `src/main/resources/db/migration/` for migration files.

### Schema Overview
- **V1**: Users and authentication (users, refresh_tokens)
- **V2**: Hospitals and subscriptions (subscription_plans, hospitals, hospital_subscriptions)
- **V3**: Doctors and patients (specializations, doctors, patients)
- **V4**: Appointments (appointment_slots, appointments)
- **V5**: Medical records (medical_records)
- **V6**: Payments (payments, platform_earnings)


## Configuration

Key configurations in `application.properties`:
- **Server Port**: 8080
- **Database**: MySQL on localhost:3306
- **JPA**: `ddl-auto: update` (auto-create tables from entities)
- **JWT Secret**: Configured for token generation
- **JWT Expiration**: 24 hours (access token), 7 days (refresh token)

## Security

The application uses JWT-based authentication. Implement security configurations in the `com.healthcare.security` package.

## Development

- Use Lombok annotations to reduce boilerplate code
- Follow the layered architecture: Controller → Service → Repository
- Add validation annotations to DTOs
- Run SQL migrations manually or let JPA auto-create tables
- Document APIs using SpringDoc annotations

## Testing

Run tests with:
```bash
mvn test
```

## Building for Production

```bash
mvn clean package
java -jar target/healthcare-app-0.0.1-SNAPSHOT.jar
```
