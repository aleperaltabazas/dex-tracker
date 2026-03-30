## Why

Both the backend (Spark + Kotlin 1.4 / Java 8) and frontend (Webpack + React 16 + Redux) are running on significantly outdated stacks, accumulating security vulnerabilities and technical debt that blocks adoption of modern tooling and developer experience improvements. This modernization clears that debt and brings both projects to current industry standards.

## What Changes

**Backend:**
- Replace Spark 2.7 with Spring Boot 3.x (latest)
- Replace Google Guice DI with Spring's built-in DI (`@Configuration` / `@Bean`)
- Upgrade Kotlin to 2.x and Java target from 8 to 17
- Replace legacy `mongodb-driver` 3.x with `mongodb-driver-sync` 5.x (keeping custom `Storage` abstraction — NOT Spring Data)
- Replace Typesafe Config (`application.conf`) with Spring Boot's `application.yml`
- Add a `docker-compose.yml` for local MongoDB development
- Update remaining dependencies: Arrow-kt 0.10→2.x, Kotest 4→5, Apache HttpClient 4→5, Google API Client

**Frontend:**
- Pin Node version to 22 (`.nvmrc` + `engines`)
- Replace Webpack 4 toolchain (+ Babel, awesome-typescript-loader) with Vite
- Upgrade TypeScript 3.x → 5.x
- Upgrade React 16 → 19 (latest stable); adopt `createRoot`
- Remove Redux + react-redux; replace with React Context + `useReducer` (one context per domain: session, pokedex, syncQueue, global)
- Replace deprecated `react-google-login` with `@react-oauth/google` (Google Identity Services)
- Upgrade React Router v5 → v6 and Material UI v4 → MUI v5 (`@emotion`)
- Update all remaining frontend dependencies

## Capabilities

### New Capabilities

- `backend-spring-boot`: Spring Boot 3 application host — HTTP routing, DI container, configuration, filters/interceptors
- `backend-mongo-connector`: MongoDB connectivity via `mongodb-driver-sync` 5.x with custom Storage abstraction
- `backend-local-dev`: Docker Compose setup for local MongoDB development environment
- `frontend-build-toolchain`: Vite-based build pipeline replacing Webpack (dev server, production build, TypeScript 5)
- `frontend-state-management`: React Context + `useReducer` replacing Redux (session, pokedex, syncQueue, global domains)
- `frontend-google-auth`: Google OAuth via `@react-oauth/google` (Google Identity Services) replacing deprecated library

### Modified Capabilities

<!-- No existing specs to delta against — this is a greenfield spec set -->

## Impact

- **Backend pom.xml**: Nearly full rewrite — parent changes to Spring Boot, most dependencies replaced
- **Backend source**: All controllers, filters, DI modules rewritten; services/models/storage mostly preserved
- **Frontend package.json**: Most dependencies replaced or removed
- **Frontend src/**: Entry point, routing, all state-consuming components updated
- **Build/deploy scripts**: May need updating for new packaging (Spring Boot fat jar, Vite output paths)
- **Local development**: New Docker Compose workflow for MongoDB
