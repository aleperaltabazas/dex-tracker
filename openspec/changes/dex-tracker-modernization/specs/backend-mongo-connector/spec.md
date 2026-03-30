## ADDED Requirements

### Requirement: MongoDB connection via driver-sync 5.x
The backend SHALL connect to MongoDB using `mongodb-driver-sync` 5.x. The legacy `mongodb-driver` 3.x dependency SHALL be removed.

#### Scenario: Application connects to MongoDB on startup
- **WHEN** the Spring Boot context initializes with valid MongoDB credentials
- **THEN** a `MongoClient` bean is created and the target database is accessible

#### Scenario: Connection string with SRV protocol is supported
- **WHEN** `db.host` is configured with a `mongodb+srv://` URI
- **THEN** the driver resolves the SRV record and connects successfully

### Requirement: Custom Storage abstraction is preserved
The `Storage` class and its query builder types (`Query`, `Update`, `Delete`, `Replace`) SHALL continue to be used as the sole data access layer. Spring Data MongoDB SHALL NOT be introduced.

#### Scenario: Storage performs reads correctly
- **WHEN** a service calls `storage.query(collection).where(filter).findOne()`
- **THEN** the correct document is returned from MongoDB

#### Scenario: Storage performs writes correctly
- **WHEN** a service calls `storage.insert(collection, value)`
- **THEN** the document is persisted in the MongoDB collection

### Requirement: MongoDB configuration as a Spring bean
The `MongoClient` and `MongoDatabase` SHALL be configured in a Spring `@Configuration` class using `MongoClientSettings` from the 5.x API.

#### Scenario: MongoDatabase bean is injectable
- **WHEN** a component declares a `MongoDatabase` dependency
- **THEN** Spring injects the correctly configured database instance
