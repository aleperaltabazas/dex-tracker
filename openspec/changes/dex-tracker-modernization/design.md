## Context

The dex-tracker project has two sub-projects sharing a repository:

- **dex-tracker-backend**: Kotlin app using Spark 2.7 (embedded Jetty), Google Guice for DI, legacy MongoDB driver 3.x, Typesafe Config, Arrow-kt 0.10, and Java 8. The app is straightforward — REST controllers, a handful of services, a custom `Storage` abstraction over MongoDB collections, and Google OAuth token validation.
- **dex-tracker-frontend**: React 16 SPA using Webpack 4 + Babel + awesome-typescript-loader, TypeScript 3, Redux (manual actions/reducers/types), `react-google-login` (deprecated), React Router v5, and Material UI v4.

Both stacks are multiple major versions behind current standards, accumulating CVEs and blocking ecosystem upgrades.

## Goals / Non-Goals

**Goals:**
- Migrate backend to Spring Boot 3 (Java 17, Kotlin 2.x), replacing Spark, Guice, and Typesafe Config
- Upgrade MongoDB driver to `mongodb-driver-sync` 5.x while keeping the custom `Storage` abstraction
- Add Docker Compose for local MongoDB
- Migrate frontend build to Vite + TypeScript 5
- Upgrade React to 19, replacing `ReactDOM.render` with `createRoot`
- Replace Redux with React Context + `useReducer` (one context per domain)
- Replace deprecated `react-google-login` with `@react-oauth/google`
- Upgrade React Router v5 → v6 and MUI v4 → v5
- Keep the existing application behavior (routes, auth flow, data model) unchanged

**Non-Goals:**
- No new product features in this change
- No database schema migration (MongoDB documents unchanged)
- No API contract changes (existing endpoints stay identical)
- No CI/CD pipeline changes (out of scope)
- No switch to Spring Data MongoDB (custom `Storage` abstraction is preserved)
- No migration to Redux Toolkit (going straight to Context instead)

## Decisions

### D1 — Spring Boot 3 as the new backend host
**Decision:** Replace Spark + embedded Jetty with Spring Boot 3 using `spring-boot-starter-web`.

**Rationale:** Spring Boot 3 is the standard Kotlin/JVM web framework, has first-class Kotlin support, handles DI/config/logging/packaging out of the box, and requires Java 17 which aligns with the LTS roadmap.

**Alternative considered:** Ktor (JetBrains' Kotlin-native framework). Rejected because Spring Boot is more familiar, has better ecosystem support for the dependencies already in use (Google OAuth, Jackson), and the migration path from Spark is more mechanical.

### D2 — Replace Guice with Spring DI
**Decision:** Remove all 13 Guice modules; re-express bindings as Spring `@Configuration` classes with `@Bean` methods. Named bindings (`@Named`) become Spring `@Qualifier`.

**Rationale:** Spring DI is built into Spring Boot — adding Guice alongside it creates dual-container complexity. The existing module structure maps cleanly to `@Configuration` classes.

**Alternative considered:** Keeping Guice via `guice-spring` bridge. Rejected — adds complexity without benefit; the Guice modules are straightforward to rewrite.

### D3 — Keep custom `Storage` abstraction (not Spring Data MongoDB)
**Decision:** Upgrade `mongodb-driver-sync` to 5.x but keep the handwritten `Storage` / `Query` / `Update` / `Delete` abstraction layer untouched (modulo API compatibility fixes).

**Rationale:** The existing abstraction is small, well-understood, and serves the app's needs. Spring Data MongoDB adds annotation-driven mapping and repository interfaces that would require rewriting the data layer — unnecessary churn with no functional benefit.

**Implication:** `DatabaseModule` → `MongoConfiguration` (`@Configuration`) wires `MongoClient` and `MongoDatabase` beans. `Storage` is registered as a `@Component` or `@Bean`.

### D4 — Vite replaces Webpack
**Decision:** Remove the entire Webpack 4 toolchain (Babel, awesome-typescript-loader, all plugins) and replace with Vite + `@vitejs/plugin-react`.

**Rationale:** Vite has native TypeScript and SCSS support, no Babel configuration required, dramatically faster dev server (native ESM), and a simpler config surface. `awesome-typescript-loader` is abandoned; Webpack 4 doesn't support TypeScript 5.

**Migration note:** Webpack builds the frontend into `../dex-tracker-backend/src/main/resources/assets/...` for production. This path is replicated in `vite.config.ts` via `build.outDir`.

### D5 — React Context + `useReducer` per domain (not Zustand, not RTK)
**Decision:** Replace Redux with four independent React Contexts (`SessionContext`, `PokedexContext`, `SyncQueueContext`, `GlobalContext`), each owning a `useReducer` and exposing a custom hook.

**Rationale:** The state is small and domain-separated (already structured this way in Redux). React Context avoids a new dependency; `useReducer` preserves the action-dispatch mental model. Zustand would work but adds a dependency; RTK would be over-engineered for this app's scale.

**Pattern per context:**
```
createContext → Provider (useReducer inside) → custom hook (useSession, usePokedex, etc.)
```

### D6 — `@react-oauth/google` for Google Auth
**Decision:** Replace `react-google-login` (abandoned, uses deprecated Google Sign-In JS SDK) with `@react-oauth/google` which wraps the current Google Identity Services (GIS) SDK.

**Rationale:** `react-google-login` triggers browser console warnings about the deprecated `accounts.google.com/gsi` API and will eventually stop working. `@react-oauth/google` is the canonical community replacement with an equivalent API surface.

**Changes:** `Login.tsx` uses `useGoogleLogin()` hook or new `<GoogleLogin>` component; `entry.tsx` wraps the app in `<GoogleOAuthProvider clientId={...}>`.

### D7 — React Router v6 migration approach
**Decision:** Migrate `<Switch>` → `<Routes>`, `component=` → `element=`, `useHistory` → `useNavigate`. Keep the same three route definitions.

**Note:** App.tsx currently has three flat routes — no nested routing complexity. Migration is mechanical.

### D8 — MUI v4 → MUI v5 (`@emotion`)
**Decision:** Replace `@material-ui/core` v4 (JSS) with `@mui/material` v5 + `@emotion/react` + `@emotion/styled`.

**Note:** Review all `makeStyles` / `withStyles` usages; migrate to `sx` prop or `styled()`.

## Risks / Trade-offs

- **Arrow-kt 0.10 → 2.x is a breaking rewrite** → Mitigation: Audit all Arrow usages in the backend before migrating; the app uses `arrow-core` and `arrow-fx` which have completely different APIs in 2.x. May simplify by removing Arrow if usage is light.
- **Spring Boot 3 requires Java 17** → Mitigation: Straightforward JDK upgrade; no runtime conflicts expected since the app is self-contained.
- **Vite output path differs from Webpack** → Mitigation: Configure `build.outDir` in `vite.config.ts` to match the existing backend resource path; verify with a production build before closing the task.
- **MUI v4 → v5 may have visual regressions** → Mitigation: Manual review of all pages (HomePage, UserPage, DexPage) after migration.
- **React 19 strict mode** → Mitigation: Run dev mode after migration and resolve any double-render warnings.
- **`SyncQueueContext` timeout management** → The Redux `syncQueue` state includes a timeout reference; Context must handle this carefully to avoid memory leaks on re-render.

## Migration Plan

**Backend (sequential — B1 must land before B2 and B4):**
1. B1: Spring Boot migration (pom.xml, main class, controllers, filters, DI, config)
2. B2: MongoDB driver upgrade (API compatibility fixes in `Storage.kt` and config)
3. B3: Docker Compose (independent — can land anytime)
4. B4: Arrow-kt, Kotest, remaining deps

**Frontend (F2 unlocks F3–F5; F1 and F6 are independent):**
1. F1: `.nvmrc` + `engines` (trivial, land first)
2. F2: Vite migration (toolchain only — app code unchanged)
3. F3 + F4 + F5 in parallel after F2 (React 19, Context, Google Auth)
4. F6: React Router v6 + MUI v5 + remaining deps (can overlap with F3–F5)

**Rollback:** Git-based — each task should be its own commit or PR. No database changes means rollback is purely code.

## Open Questions

- Does the backend currently run on Heroku (heroku-maven-plugin in pom.xml)? If so, the Spring Boot fat jar packaging and Procfile may need updating. → Confirm deployment target before closing B1.
- Are there any Arrow-kt usages in service/business logic that are load-bearing, or is it only used for utility types? → Audit before B4 to decide whether to migrate or remove Arrow.
