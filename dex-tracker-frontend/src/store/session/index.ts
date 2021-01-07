export const INVALIDATE_SESSION = "INVALIDATE_SESSION";
export const LOG_IN_ACTION = "LOG_IN_ACTION";

interface InvalidateSessionAction {
  type: typeof INVALIDATE_SESSION;
}

interface LogInAction {
  type: typeof LOG_IN_ACTION;
  payload: LoggedInState;
}

export type SessionAction = InvalidateSessionAction | LogInAction;

export interface LoggedInState {
  token: string;
  isLoggedIn: true;
}

export interface NotLoggedInState {
  isLoggedIn: false;
}

export type SessionState = LoggedInState | NotLoggedInState;
