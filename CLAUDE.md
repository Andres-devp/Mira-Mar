# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository layout

This is a **two-project monorepo** for the Mira Mar boutique-hotel platform:

- `Angular/MiraMar/` — Angular 16 SPA (the active frontend).
- `SprintBoot Web/demo/` — Spring Boot 4.0.3 / Java 17 REST backend.
- `Documents/` — class & ER diagrams (reference only).

The root `README.md` describes an older Thymeleaf-based architecture. **The active frontend is the Angular SPA.** The Thymeleaf templates that still live under `SprintBoot Web/demo/src/main/resources/templates/` are legacy and not wired to current routes.

## Common commands

### Backend (`SprintBoot Web/demo/`)

```sh
./mvnw spring-boot:run     # run dev server on http://localhost:8080
./mvnw test                # run all tests
./mvnw -Dtest=ClassName test       # run a single test class
./mvnw -Dtest=ClassName#method test # run a single test method
./mvnw package             # build jar
```

On Windows use `mvnw.cmd`.

- Swagger UI: `http://localhost:8080/swagger-ui/index.html` (the README's `:8090` is stale).
- H2 console: `http://localhost:8080/h2` — JDBC URL `jdbc:h2:file:./mydatabase`, user `sa`, blank password.

### Frontend (`Angular/MiraMar/`)

```sh
npm install
npm start                  # ng serve on http://localhost:4200
npm run build              # production build to dist/
npm run watch              # dev build with --watch
npm test                   # Karma + Jasmine
ng test --include='**/foo.component.spec.ts'   # single spec
```

Both servers must run together: the SPA hits `http://localhost:8080` and the backend's `CorsConfig` only allows `http://localhost:4200`.

## Architecture

### Backend (Spring Boot)

Single Maven module under `com.example.demo` with the standard layered split:

- `entities/` — JPA entities. Field names are in **Spanish** (`nombre`, `contrasena`, `usuario`, `fechaInicio`, etc.). Lombok `@Builder`/`@Data` is used widely — generated getters/setters won't show in the source.
- `repository/` — Spring Data JPA interfaces.
- `service/` — Each service has an interface + `…Impl` (e.g. `AuthService` / `AuthServiceImpl`).
- `controller/` — `@RestController`s under `/auth`, `/rooms`, `/roomtypes`, `/services`, `/reservations`, `/usuarios`, `/admin`, `/operator`.
- `controller/dto/` — request/response DTOs (entities are not exposed directly).
- `config/` — `CorsConfig` (allows `:4200` with credentials), `DataLoader` (seed data), `H2ConsoleConfig`.
- `exception/` + `handler/` — domain exceptions and global handlers.

#### Domain model

Three user types are **separate tables**, not a single `User` with a role column: `Client`, `Operator`, `Administrator`. Login walks all three repos in `AuthServiceImpl#autenticar` and returns a role tag (`ADMIN`, `OPERATOR`, `CLIENT`).

Reservation flow: `Client` ⇄ `Reservation` ⇄ `Room` → `RoomType`. Each `Reservation` owns one `Account` (states: `OPEN`, `CLOSED`). `AccountItem` is the join row between `Account` and `HotelService`, holding `cantidad`, `precioUnitario`, `subtotal`, soft-delete via `eliminado`. Reservation states: `CONFIRMED`, `PENDING`, `CANCELED`.

#### Data lifecycle — important

`application.properties` sets `spring.jpa.hibernate.ddl-auto=create-drop`. **Every restart drops and recreates all tables**, even though `spring.datasource.url` points at a file (`./mydatabase`). `DataLoader` runs as a `CommandLineRunner` and re-seeds when `RoomType` count is 0 (i.e. on every restart). Don't rely on persisted state between runs; if you need durable data, change `ddl-auto` first.

#### Auth model

Cookie-based, **not JWT**. `AuthController#login` issues a `user_session` cookie whose value is the raw user id (HttpOnly, 24h, path `/`). Logout clears it. Passwords are stored and compared in **plaintext** in `AuthServiceImpl` — this is a known property of the project, not a bug to "fix" silently.

### Frontend (Angular 16)

Classic `NgModule` style (not standalone components). Everything is declared in `src/app/app.module.ts` and routed in `src/app/app-routing.module.ts`.

- `core/services/` — one HTTP service per domain (`auth.service.ts`, `room.service.ts`, etc.). All point at `http://localhost:8080`.
- `core/interceptors/auth.interceptor.ts` — sets `withCredentials: true` on every request so the `user_session` cookie rides along. Registered globally in `app.module.ts` via `HTTP_INTERCEPTORS`.
- `core/models/entities/` — TypeScript mirrors of backend DTOs/entities. Re-exported from `index.ts`.
- `features/` — one folder per feature (`admin`, `auth`, `error`, `hotel-service`, `landing`, `operator`, `reservations`, `rooms`, `room-type`, `usuarios`). Each has its own `pages/` subfolder; there are no per-feature modules.
- `shared/components/` — reusable UI (navbar, footer, cards).

#### Routing conventions

In `app-routing.module.ts`, **static segments must be declared before parameterized siblings** (e.g. `rooms/table` and `rooms/add` come before `rooms/:id`). Breaking that order silently routes table/add pages into the detail component. Dashboard, auth, and error routes pass `data: { showNavbar: false, showFooter: false }` — the root layout reads this to hide chrome.

#### Session storage

`AuthService` keeps a parallel copy of the session in `localStorage` under `miramar_session` (id + role) for UI gating. The cookie is the source of truth for the backend; the localStorage entry is just so the SPA doesn't have to round-trip to know who's logged in. Clear both on logout.
