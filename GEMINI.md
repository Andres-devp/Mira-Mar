# Mira Mar Caribbean Luxury - Project Context

## Project Overview
Mira Mar is an integrated management platform for a boutique luxury resort. The system handles guest interfaces, room management (CRUD), service bookings (Spa, Tours, Restaurant), and a robust administrative panel for inventory and user control.

The project is currently in a hybrid state, featuring a **Spring Boot** backend that serves both a legacy **Thymeleaf** frontend and a modern **Angular** application.

## Tech Stack
- **Backend:** Java 17, Spring Boot, Spring Data JPA, H2 Database (In-memory).
- **Frontend (Modern):** Angular 16, TypeScript, Tailwind CSS.
- **Frontend (Legacy):** Thymeleaf, Bootstrap 5, native CSS3.
- **Documentation:** Swagger UI (OpenAPI 3).

## Directory Structure
- `Angular/MiraMar/`: Modern Angular frontend implementation.
- `SprintBoot Web/demo/`: Main backend application (Spring Boot).
  - `src/main/java/com/example/demo/`: Java source code (Controllers, Services, Entities).
  - `src/main/resources/templates/`: Thymeleaf templates.
  - `src/main/resources/static/`: Static assets (CSS, JS, images).
- `Documents/`: Project diagrams (ERD, Class Diagram) and PDF specifications.

## Building and Running

### Backend (Spring Boot)
1. Navigate to the backend directory:
   ```bash
   cd "SprintBoot Web/demo"
   ```
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Access points:
   - **API Base:** `http://localhost:8080`
   - **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`
   - **H2 Console:** `http://localhost:8080/h2` (Credentials: User: `sa`, Password: `[empty]`)

### Frontend (Angular)
1. Navigate to the frontend directory:
   ```bash
   cd "Angular/MiraMar"
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```
4. Access at: `http://localhost:4200`

## Development Conventions

### Backend
- **Architecture:** Controller -> Service -> Repository -> Entity.
- **Entities:** Located in `com.example.demo.entities`.
- **API Controllers:** Use `@RestController` and `@CrossOrigin(origins = "http://localhost:4200")`.
- **Persistence:** H2 file-based database (`./mydatabase.mv.db`). The schema is recreated on every restart (`create-drop`).

### Frontend (Angular)
- **Structure:**
  - `core/`: Centralized services, models, and interceptors.
  - `features/`: Module-specific components.
  - `shared/`: Reusable components, pipes, and directives.
- **API Services:** All backend communication is handled via services in `src/app/core/services/` (e.g., `RoomService`, `AuthService`).

## Key Files
- `SprintBoot Web/demo/pom.xml`: Backend dependencies.
- `Angular/MiraMar/package.json`: Frontend dependencies.
- `SprintBoot Web/demo/src/main/resources/application.properties`: Backend configuration.
- `Documents/DiagramaEntidad-Relación.jpg`: Database schema reference.

## Current State
The project is "En Desarrollo" (In Development). Most core modules like Rooms and Services are implemented as REST APIs, which the Angular frontend consumes. The legacy Thymeleaf templates are still present in the resources folder.
