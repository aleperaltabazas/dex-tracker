import { User, UserDexRef } from "../../types/user";

export const INVALIDATE_SESSION = "INVALIDATE_SESSION";
export const LOG_IN_ACTION = "LOG_IN_ACTION";
export const LOG_IN_ERROR = "LOG_IN_ERROR";
export const ADD_USER_DEX = "ADD_USER_DEX";
export const UPDATE_CAUGHT = "UPDATE_CAUGHT";
export const UPDATE_PICTURE = "UPDATE_PICTURE";
export const NOT_LOGGED_IN = "NOT_LOGGED_IN";

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

interface UpdatePictureAction {
  type: typeof UPDATE_PICTURE;
  payload: string;
}

interface NotLoggedInAction {
  type: typeof NOT_LOGGED_IN;
}

export type SessionAction =
  | InvalidateSessionAction
  | LogInAction
  | LoginErrorAction
  | AddUserDexAction
  | UpdateCaughtAction
  | UpdatePictureAction
  | NotLoggedInAction;

export interface LoggedInState {
  token: string;
  user: User;
  picture?: string;
  type: "LOGGED_IN";
}

export interface UninitializedState {
  type: "UNINITIALIZED";
}

export interface NotLoggedInState {
  type: "NOT_LOGGED_IN";
  localDex: UserDexRef[];
}

export interface LoginErrorState {
  type: "ERROR";
}

export type SessionState =
  | LoggedInState
  | NotLoggedInState
  | UninitializedState
  | LoginErrorState;
