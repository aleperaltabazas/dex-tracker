## ADDED Requirements

### Requirement: React Context replaces Redux for state management
The frontend SHALL use React Context with `useReducer` for all application state. Redux and react-redux SHALL be removed entirely.

#### Scenario: Session state is accessible via context hook
- **WHEN** a component calls `useSession()`
- **THEN** it receives the current session state and a dispatch function

#### Scenario: Pokedex state is accessible via context hook
- **WHEN** a component calls `usePokedex()`
- **THEN** it receives the current pokedex data and a dispatch function

#### Scenario: SyncQueue state is accessible via context hook
- **WHEN** a component calls `useSyncQueue()`
- **THEN** it receives the current sync queue and a dispatch function

#### Scenario: Global UI state is accessible via context hook
- **WHEN** a component calls `useGlobal()`
- **THEN** it receives the current global UI state (menuOpen, createDexFormOpen) and a dispatch function

### Requirement: One context provider per state domain
The application SHALL define four independent contexts: `SessionContext`, `PokedexContext`, `SyncQueueContext`, and `GlobalContext`. Each SHALL expose its own `Provider` and custom hook.

#### Scenario: Context providers wrap the app
- **WHEN** the application mounts
- **THEN** all four providers are active and their state is independently initialized

### Requirement: State shape is preserved
The state shape for each domain SHALL remain identical to the Redux state shape so that consuming components require only hook-call changes (not logic changes).

#### Scenario: Session state shape matches Redux shape
- **WHEN** `useSession()` returns state
- **THEN** the state object has the same fields as the previous Redux `SessionState`
