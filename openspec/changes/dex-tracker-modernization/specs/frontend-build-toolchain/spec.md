## ADDED Requirements

### Requirement: Vite replaces Webpack as the build tool
The frontend SHALL use Vite as its build and dev server tool. All Webpack, Babel, and related toolchain packages SHALL be removed.

#### Scenario: Dev server starts
- **WHEN** a developer runs `npm run dev`
- **THEN** Vite starts a dev server with hot module replacement

#### Scenario: Production build succeeds
- **WHEN** `npm run build` is executed
- **THEN** Vite outputs optimized static assets to the configured output directory

#### Scenario: Production output path matches backend resource path
- **WHEN** a production build runs
- **THEN** assets are written to the same path previously used by Webpack (`../dex-tracker-backend/src/main/resources/assets/...`)

### Requirement: TypeScript 5.x
The frontend SHALL use TypeScript 5.x. The `tsconfig.json` SHALL be updated to use `"moduleResolution": "bundler"` for Vite compatibility.

#### Scenario: TypeScript compilation succeeds
- **WHEN** `tsc --noEmit` is run
- **THEN** no type errors are reported

### Requirement: SCSS support in Vite
The Vite config SHALL support SCSS stylesheets without additional manual configuration beyond declaring the dependency.

#### Scenario: SCSS files are compiled
- **WHEN** a component imports a `.scss` file
- **THEN** styles are applied correctly in both dev and production builds

### Requirement: Node 22 is the required runtime
The project SHALL declare Node 22 as the required runtime via `.nvmrc` and the `engines` field in `package.json`.

#### Scenario: Node version is declared
- **WHEN** a developer runs `nvm use`
- **THEN** Node 22 is selected
