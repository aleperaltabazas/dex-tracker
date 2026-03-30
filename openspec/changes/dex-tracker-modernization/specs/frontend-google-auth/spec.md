## ADDED Requirements

### Requirement: Google OAuth via @react-oauth/google
The frontend SHALL use `@react-oauth/google` for Google OAuth login. The deprecated `react-google-login` package SHALL be removed.

#### Scenario: App is wrapped with GoogleOAuthProvider
- **WHEN** the application mounts
- **THEN** `<GoogleOAuthProvider clientId={...}>` wraps the component tree

#### Scenario: User can log in with Google
- **WHEN** a user clicks the login button and completes the Google OAuth flow
- **THEN** the app receives a valid credential response containing an `id_token`

#### Scenario: Login posts id_token to backend
- **WHEN** the OAuth callback fires with a successful credential
- **THEN** the app posts the `id_token` to `POST /api/v1/login` and updates session state on success

### Requirement: Login failure is handled gracefully
The application SHALL handle Google OAuth failures without crashing.

#### Scenario: OAuth error is handled
- **WHEN** the Google OAuth flow fails or is cancelled
- **THEN** the session state is set to ERROR and no unhandled exception is thrown
