## ADDED Requirements

### Requirement: Application boots via Spring Boot
The backend SHALL use Spring Boot 3.x as its application host, replacing Spark and embedded Jetty. The entry point SHALL call `SpringApplication.run()`.

#### Scenario: Application starts successfully
- **WHEN** the application is launched with no arguments
- **THEN** the Spring Boot context loads and the HTTP server starts on the configured port (default 9290)

#### Scenario: Port is configurable via environment variable
- **WHEN** the `PORT` environment variable is set
- **THEN** the application listens on that port

### Requirement: HTTP routing via Spring MVC
All existing REST endpoints SHALL be implemented as Spring MVC `@RestController` classes, preserving exact route paths and HTTP methods.

#### Scenario: Existing routes are reachable
- **WHEN** a client sends a request to any previously defined route (e.g., `GET /api/v1/pokedex`)
- **THEN** the response is identical to the Spark implementation

### Requirement: Dependency injection via Spring
All application components SHALL be wired via Spring DI (`@Configuration`, `@Bean`, `@Component`). Guice SHALL be removed entirely.

#### Scenario: All beans are available at startup
- **WHEN** the Spring context initializes
- **THEN** all services, storage, and infrastructure beans are resolved without errors

### Requirement: Configuration via application.yml
Application configuration SHALL be expressed in `application.yml` using Spring Boot's config system. Typesafe Config (`application.conf`) SHALL be removed.

#### Scenario: Dev config loads defaults
- **WHEN** no environment overrides are present
- **THEN** the application uses values defined in `application.yml` (e.g., `localhost:27017` for MongoDB)

#### Scenario: Secrets are overridden by environment variables
- **WHEN** environment variables such as `DATABASE_USERNAME` are set
- **THEN** those values take precedence over `application.yml` defaults

### Requirement: Request logging and tracking via Spring interceptors
The logging and tracking cross-cutting concerns SHALL be implemented as Spring `HandlerInterceptor` or `OncePerRequestFilter`, preserving MDC fields (`requestId`, `dexClient`, `motive`).

#### Scenario: Each request gets a unique requestId
- **WHEN** any HTTP request is received
- **THEN** a `requestId` is added to the MDC and appears in log output

### Requirement: Java 17 and Kotlin 2.x
The backend SHALL target Java 17 as the minimum JVM version and use Kotlin 2.x.

#### Scenario: Build succeeds on Java 17
- **WHEN** `mvn package` is run on a Java 17 JDK
- **THEN** the build completes without errors
