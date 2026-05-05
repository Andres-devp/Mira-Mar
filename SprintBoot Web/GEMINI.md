# Mira Mar Caribbean Luxury - Spring Boot Backend

## Project Overview
This directory contains the Spring Boot backend for the Mira Mar Caribbean Luxury project. It serves as a RESTful API provider for the modern Angular frontend and includes legacy Thymeleaf templates for older web interfaces. The application manages core hotel operations, including rooms, services, reservations, users, and accounting.

**Key Technologies:**
- **Framework:** Java 17, Spring Boot (WebMVC, Data JPA)
- **Database:** H2 (In-memory/File-based database)
- **Documentation:** Swagger/OpenAPI 3 via Springdoc
- **Build Tool:** Maven
- **Boilerplate Reduction:** Lombok

## Directory Structure
- `demo/src/main/java/com/example/demo/`: Main Java source code containing configurations, controllers, exceptions, handlers, repositories, services, and entities (e.g., `Room`, `Reservation`, `Client`, `HotelService`).
- `demo/src/main/resources/application.properties`: Configuration file for the database, logging, and Spring properties.
- `demo/src/main/resources/static/`: Static assets (CSS, JS, Images) for legacy web interfaces.
- `demo/src/main/resources/templates/`: Thymeleaf HTML templates for legacy web pages.
- `mydatabase.mv.db`: The local H2 file-based database.

## Building and Running

### Prerequisites
- Java 17

### Running the Application
To run the Spring Boot application using the Maven wrapper, navigate into the `demo` directory:

```bash
cd demo
./mvnw spring-boot:run
```
*(On Windows Command Prompt or PowerShell, use `mvnw.cmd spring-boot:run`)*

### Access Points
- **API Base URL:** `http://localhost:8080`
- **Swagger UI (API Docs):** `http://localhost:8080/swagger-ui/index.html` (Accessible when the app is running)
- **H2 Database Console:** `http://localhost:8080/h2`
  - **JDBC URL:** `jdbc:h2:file:./mydatabase`
  - **Username:** `sa`
  - **Password:** *(empty)*

## Development Conventions
- **Architecture:** Standard Layered Architecture (`Controller` -> `Service` -> `Repository` -> `Entity`).
- **Database Schema:** Uses `create-drop` (`spring.jpa.hibernate.ddl-auto=create-drop`), meaning the database schema is dropped and re-created every time the application starts. The SQL init mode is set to always.
- **CORS:** Controllers should use `@CrossOrigin(origins = "http://localhost:4200")` to allow communication with the Angular frontend.
- **Logging:** SQL statements and parameter bindings are printed to the console (`DEBUG` and `TRACE` levels respectively) for easier debugging during development.
- **Lombok:** Used extensively for reducing boilerplate code (getters, setters, constructors). Ensure annotation processing is enabled in your IDE.

## Usage
This directory is intended to be the backend service of the Mira Mar project. All new API endpoints should be documented via Swagger/OpenAPI annotations and follow RESTful principles to be consumed by the Angular frontend application.
