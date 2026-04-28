# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Quick Start

```bash
# Terminal 1 — Backend
cd "SprintBoot Web/demo"
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
# Serves on http://localhost:8080

# Terminal 2 — Frontend
cd Angular/MiraMar
npm ci
npm start
# Serves on http://localhost:4200
```

Test credentials: `admin/123`, `operador/123`, `cliente/123`

## Commands

### Frontend (`Angular/MiraMar/`)
```bash
npm start          # dev server
npm run build      # production build
npm test           # Karma/Jasmine unit tests
npm run watch      # rebuild on change
```

### Backend (`SprintBoot Web/demo/`)
```bash
mvnw.cmd spring-boot:run              # run app
mvnw.cmd test                         # all tests
mvnw.cmd -Dtest=ClassName test        # single test class
mvnw.cmd clean package                # full build
```

Always run Maven from `SprintBoot Web/demo/` — H2 database file (`mydatabase.mv.db`) is created relative to the working directory.

## Architecture

**Stack**: Angular 16 (TypeScript) + Spring Boot 3 (Java 17) + H2 embedded DB

### Frontend (`Angular/MiraMar/src/app/`)
- `app.module.ts` — all declarations and imports (single module, no lazy loading)
- `app-routing.module.ts` — all routes; static routes (`/rooms/table`) come before parameterized routes (`/rooms/:id`) intentionally
- `app.component.ts` — controls navbar/footer visibility based on current route (hidden on `/admin`, `/operator`)
- `core/interceptors/auth.interceptor.ts` — adds `withCredentials: true` to every HTTP request
- `core/services/` — one service per entity, each hardcodes `http://localhost:8080/<resource>`
- `core/models/entities/` — TypeScript interfaces matching backend entities
- `features/` — one folder per feature (auth, landing, rooms, reservations, hotel-service, usuarios, admin, operator)
- `shared/components/` — navbar, footer, room-card, amenity-card, dining-card

### Backend (`SprintBoot Web/demo/src/main/java/com/example/demo/`)
- Standard layered architecture: `controller → service (interface + impl) → repository → entities`
- `config/DataLoader.java` — seeds demo data on every startup
- `config/CorsConfig.java` — hardcodes `http://localhost:4200` as allowed origin
- Swagger UI at `http://localhost:8080/swagger-ui/index.html`

### Auth Flow
1. `POST /auth/login` validates against Client/Administrator/Operator tables
2. Backend sets HTTP-only `user_session` cookie and returns `{ id, rol }`
3. Frontend stores session in `localStorage` key `miramar_session`
4. `AuthService.isLoggedIn()` reads localStorage; `AuthInterceptor` sends cookie via `withCredentials`
5. Roles: `Client`, `Administrator`, `Operator`

### Database
- H2 file-based at `./mydatabase`; schema is `create-drop` — **resets on every backend restart**
- H2 console at `http://localhost:8080/h2`
- Passwords stored in plaintext (no hashing)

## API Conventions

Non-standard patterns the frontend depends on — don't change without updating Angular services:
- List endpoints: `GET /resource/all` (not `GET /resource`)
- Create endpoints: `POST /resource/add` (not `POST /resource`)

Key endpoints: `/auth/{login,register,logout}`, `/rooms`, `/reservations`, `/services`, `/roomtypes`, `/users`, `/admin/users`, `/admin/stats`

## Adding a Feature

1. **Backend**: entity → repository → service interface + impl → controller with `@CrossOrigin(origins = "http://localhost:4200")`
2. **Frontend**: model interface in `core/models/entities/` → service in `core/services/` → components in `features/<name>/` → routes in `app-routing.module.ts` → declare in `app.module.ts`

## Known Issues

- **CORS**: If frontend port changes from 4200, update `@CrossOrigin` on all controllers and `CorsConfig.java`
- **Plaintext passwords**: `AuthServiceImpl` stores passwords as-is; add BCrypt before any production use
- **No route guards**: Admin/operator routes are not protected on the frontend
