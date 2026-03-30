## ADDED Requirements

### Requirement: Docker Compose file for local MongoDB
The repository SHALL include a `docker-compose.yml` that starts a local MongoDB instance suitable for development.

#### Scenario: MongoDB starts with docker-compose up
- **WHEN** a developer runs `docker compose up` in the project directory
- **THEN** a MongoDB instance is accessible on `localhost:27017`

#### Scenario: Data persists across restarts
- **WHEN** the MongoDB container is stopped and restarted
- **THEN** previously inserted data is still present (named volume)

### Requirement: Dev credentials match application.yml defaults
The Docker Compose MongoDB credentials SHALL match the default values configured in `application.yml` so no manual setup is required for local development.

#### Scenario: Backend connects to local MongoDB without extra config
- **WHEN** the backend is started locally with default `application.yml` values
- **THEN** it connects successfully to the Docker Compose MongoDB instance without any credential overrides
