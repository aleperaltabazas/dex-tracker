## 1. Backend — Spring Boot Migration (B1)

- [x] 1.1 Update `pom.xml`: add `spring-boot-starter-parent` as parent, remove Spark, Jetty, Servlet, Guice, Typesafe Config dependencies; set Java 17 and Kotlin 2.x versions
- [x] 1.2 Add `spring-boot-starter-web` and `kotlin-spring` / `kotlin-allopen` compiler plugins
- [x] 1.3 Replace `fun main()` + Jetty/Spark server setup with `SpringApplication.run()`
- [x] 1.4 Convert each Guice module to a Spring `@Configuration` class with `@Bean` methods; replace `@Named` with `@Qualifier`
- [x] 1.5 Rewrite all Spark `Controller` implementations as Spring `@RestController` classes, preserving exact route paths and HTTP methods
- [x] 1.6 Replace `LoggingFilter` and `TrackingFilter` with Spring `OncePerRequestFilter` implementations preserving MDC fields (`requestId`, `dexClient`, `motive`)
- [x] 1.7 Replace `application.conf` with `application.yml`; replace all `config.getString(...)` usages with `@Value` or `@ConfigurationProperties`
- [x] 1.8 Update `maven-surefire-plugin` and `maven-assembly-plugin` configuration for Java 17 and Spring Boot fat jar packaging
- [x] 1.9 Verify the app starts and all routes respond correctly

## 2. Backend — MongoDB Driver Upgrade (B2)

- [x] 2.1 Replace `mongodb-driver` 3.x with `mongodb-driver-sync` 5.x in `pom.xml`
- [x] 2.2 Rewrite the `MongoConfiguration` (formerly `DatabaseModule`) `@Bean` methods using the 5.x `MongoClientSettings` and `MongoClients` API
- [x] 2.3 Fix any deprecated or removed API calls in `Storage.kt` (e.g., codec registry, cursor iteration)
- [x] 2.4 Verify reads and writes work correctly against a local MongoDB instance

## 3. Backend — Local Dev Environment (B3)

- [x] 3.1 Create `docker-compose.yml` at the repository root with a `mongo` service on port 27017, a named volume, and credentials matching `application.yml` dev defaults
- [x] 3.2 Confirm `docker compose up` starts MongoDB and the backend connects without extra configuration

## 4. Backend — Remaining Dependency Updates (B4)

- [x] 4.1 Audit all Arrow-kt usages in the codebase; decide whether to migrate to Arrow 2.x or remove the dependency
- [x] 4.2 Migrate (or remove) Arrow-kt from 0.10.x to 2.x, resolving API breaking changes
- [x] 4.3 Upgrade Kotest from 4.x to 5.x; fix any breaking changes in test files
- [x] 4.4 Upgrade Apache HttpClient to 5.x (or replace with Spring's `RestClient` / `WebClient`)
- [x] 4.5 Upgrade Google API Client library to latest stable
- [x] 4.6 Remove any Jackson version overrides now managed by Spring Boot

## 5. Frontend — Icons (F0)

- [x] 5.0 Update `src/icons.scss`: replace all `@import "icons/genX.css"` with `@import url('/icons/genX.css')` so they load from `public/icons/` via Vite's static asset serving

## 6. Frontend — Node Version and Build Toolchain (F1 + F2)

- [x] 5.1 Add `.nvmrc` file with content `22`; add `"engines": { "node": ">=22" }` to `package.json`
- [x] 5.2 Remove Webpack, Babel, awesome-typescript-loader, and all related packages from `package.json`
- [x] 5.3 Delete `webpack.config.js`, `webpack.plugins.js`, `webpack.utils.js`
- [x] 5.4 Add `vite` and `@vitejs/plugin-react` as dev dependencies
- [x] 5.5 Create `vite.config.ts` with React plugin, SCSS support, dev proxy to backend (port 9290), and `build.outDir` pointing to the backend resources path
- [x] 5.6 Add `index.html` at the frontend project root as Vite's entry point (referencing `src/entry.tsx`)
- [x] 5.7 Update `package.json` scripts: `dev` → `vite`, `build` → `vite build`, `preview` → `vite preview`
- [x] 5.8 Upgrade TypeScript to 5.x; update `tsconfig.json` (`"moduleResolution": "bundler"`, remove `noEmit` if Vite handles emission)
- [x] 5.9 Verify `npm run dev` starts the Vite dev server and `npm run build` produces a valid production bundle

## 6. Frontend — React 19 Migration (F3)

- [x] 6.1 Upgrade `react`, `react-dom`, `@types/react`, `@types/react-dom` to React 19 (latest stable)
- [x] 6.2 Replace `ReactDOM.render(app, el)` in `entry.tsx` with `createRoot(el).render(app)`
- [x] 6.3 Resolve any React 19 breaking changes (strict mode double-invoke, removed legacy lifecycle warnings, etc.)
- [x] 6.4 Verify all pages render without console errors in development mode

## 7. Frontend — State Management: Redux → React Context (F4)

- [x] 7.1 Create `src/contexts/session.tsx`: `SessionContext`, `SessionProvider`, `useSession()` hook with `useReducer` mirroring existing Redux session state shape
- [x] 7.2 Create `src/contexts/pokedex.tsx`: `PokedexContext`, `PokedexProvider`, `usePokedex()` hook
- [x] 7.3 Create `src/contexts/syncQueue.tsx`: `SyncQueueContext`, `SyncQueueProvider`, `useSyncQueue()` hook (handle timeout ref carefully to avoid memory leaks)
- [x] 7.4 Create `src/contexts/global.tsx`: `GlobalContext`, `GlobalProvider`, `useGlobal()` hook
- [x] 7.5 Update `entry.tsx`: remove `<Provider store={store}>`, compose the four context providers
- [x] 7.6 Update all components using `useSelector` / `useDispatch` to use the corresponding context hooks
- [x] 7.7 Delete `src/store/`, `src/actions/`, `src/reducers/`, `src/store.ts`
- [x] 7.8 Remove `redux`, `react-redux`, `@types/react-redux` from `package.json`
- [x] 7.9 Verify app state behaves identically (login, pokedex load, sync queue, menu open/close)

## 8. Frontend — Google Auth Migration (F5)

- [x] 8.1 Remove `react-google-login` from `package.json`
- [x] 8.2 Add `@react-oauth/google` to `package.json`
- [x] 8.3 Wrap the app with `<GoogleOAuthProvider clientId={...}>` in `entry.tsx`
- [x] 8.4 Update `Login.tsx`: replace `<GoogleLogin render={...}>` with the `@react-oauth/google` API (`useGoogleLogin()` hook or new `<GoogleLogin>` component)
- [x] 8.5 Update `functions/login.ts` if the credential response shape has changed (ensure `id_token` is correctly extracted and posted to `/api/v1/login`)
- [x] 8.6 Test the full login flow end-to-end (OAuth popup → backend token validation → session state updated)

## 9. Frontend — Remaining Dependency Updates (F6)

- [x] 9.1 Upgrade React Router v5 → v6: replace `<Switch>` with `<Routes>`, `component=` with `element=`, `useHistory` with `useNavigate` in `App.tsx` and any other routing code
- [x] 9.2 Replace `@material-ui/core` and `@material-ui/icons` v4 with `@mui/material` and `@mui/icons-material` v5; add `@emotion/react` and `@emotion/styled`
- [x] 9.3 Migrate any `makeStyles` / `withStyles` usages to MUI v5 `sx` prop or `styled()` (used `@mui/styles` compatibility layer with JSS — `Hidden` removed, replaced with `Box` + `sx`)
- [x] 9.4 Remove legacy `material-ui` v0.x package (present alongside v4 in current `package.json`)
- [x] 9.5 Upgrade `react-loader-spinner`, `js-cookie`, `classnames`, `react-window`, `reactjs-popup` to latest versions; resolve any breaking changes
- [x] 9.6 Remove `node-pom-parser` (no longer needed without Webpack)
- [x] 9.7 Remove `axios` from devDependencies if not used directly (was in devDeps, check usage) — axios used in functions/; upgraded to latest and moved to regular dep
- [x] 9.8 Run a full visual review of all three pages (HomePage, UserPage, DexPage) after MUI and Router upgrades
