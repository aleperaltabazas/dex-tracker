import { User, UserDex, UserDexRef } from "../../types/user";

export const INVALIDATE_SESSION = "INVALIDATE_SESSION";
export const LOG_IN_ACTION = "LOG_IN_ACTION";
export const LOG_IN_ERROR = "LOG_IN_ERROR";
export const ADD_USER_DEX = "ADD_USER_DEX";
export const UPDATE_CAUGHT = "UPDATE_CAUGHT";

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

interface AddUserDexAction {
  type: typeof ADD_USER_DEX;
  payload: UserDexRef;
}

interface UpdateCaughtAction {
  type: typeof UPDATE_CAUGHT;
  payload: {
    dexId: string;
    update: (current: number) => number;
  };
}

export type SessionAction =
  | InvalidateSessionAction
  | LogInAction
  | LoginErrorAction
  | AddUserDexAction
  | UpdateCaughtAction;

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
