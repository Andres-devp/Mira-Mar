# AGENTS.md

## Repo shape (not a single workspace)
- Backend API lives in `SprintBoot Web/demo` (Spring Boot + Maven wrapper).
- Frontend SPA lives in `Angular/MiraMar` (Angular CLI app).
- Do not run `npm` at repo root; the real Node project is `Angular/MiraMar` (root `package-lock.json` is a stub).
- `SprintBoot Web` contains a space; quote the path in raw shell commands.

## Canonical commands
- Backend (`SprintBoot Web/demo`): `mvnw.cmd spring-boot:run`, `mvnw.cmd test`, `mvnw.cmd clean package`.
- Backend single test class: `mvnw.cmd -Dtest=DemoApplicationTests test` (or swap class name).
- Frontend (`Angular/MiraMar`): `npm ci`, `npm start`, `npm run build`, `npm test`.

## Runtime wiring you can break easily
- Backend runs on `8080` (`src/main/resources/application.properties`), even though root README mentions `8090`.
- Angular services hardcode `http://localhost:8080/...` in `Angular/MiraMar/src/app/core/services/*.ts`.
- Backend controllers hardcode CORS to `http://localhost:4200` via `@CrossOrigin`.
- API convention is not pure REST naming: list/create endpoints use `/all` and `/add` (frontend depends on this).

## Data and local-state gotchas
- H2 uses `jdbc:h2:file:./mydatabase` (path is relative to the backend working directory).
- Run Maven commands from `SprintBoot Web/demo`; running from elsewhere creates DB files in unexpected locations.
- `spring.jpa.hibernate.ddl-auto=create-drop` resets schema each run; startup reseeds demo data through `config/DataLoader.java`.
- Seed credentials for quick manual checks: `admin/123`, `operador/123`, `cliente/123`.

## Architecture pointers for fast edits
- Backend entrypoint: `SprintBoot Web/demo/src/main/java/com/example/demo/DemoApplication.java`.
- Backend is layered `controller -> service -> repository -> entities` under `com.example.demo`.
- Frontend entrypoint: `Angular/MiraMar/src/main.ts`; feature code is in `Angular/MiraMar/src/app/features`.
- `Angular/MiraMar/src/app/app-routing.module.ts` intentionally places static routes before parameterized routes; keep that order.
- `src/main/resources/templates` exists, but current Java endpoints are all `@RestController` (Angular is the active UI path).

## Minimal verification expectations
- Backend changes: run `mvnw.cmd test`.
- Frontend changes: run `npm run build` (plus `npm test` when touching component/service logic).
- Endpoint contract changes: verify at least one affected Angular screen against the running backend.
