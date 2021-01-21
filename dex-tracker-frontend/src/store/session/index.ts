import { User } from "../../types/user";

export const INVALIDATE_SESSION = "INVALIDATE_SESSION";
export const LOG_IN_ACTION = "LOG_IN_ACTION";
export const LOG_IN_ERROR = "LOG_IN_ERROR";

interface InvalidateSessionAction {
  type: typeof INVALIDATE_SESSION;
}

interface LogInAction {
  type: typeof LOG_IN_ACTION;
  payload: LoggedInState;
}

interface LoginErrorAction {
  type: typeof LOG_IN_ERROR;
}

export type SessionAction =
  | InvalidateSessionAction
  | LogInAction
  | LoginErrorAction;

export interface LoggedInState {
  token: string;
  user: User;
  isLoggedIn: true;
  isError: false;
}

export interface NotLoggedInState {
  isLoggedIn: false;
  isError: false;
}

export interface LoginErrorState {
  isLoggedIn: false;
  isError: true;
}

export type SessionState = LoggedInState | NotLoggedInState | LoginErrorState;
