import { User, UserDex } from "../../types/user";

export const INVALIDATE_SESSION = "INVALIDATE_SESSION";
export const LOG_IN_ACTION = "LOG_IN_ACTION";
export const LOG_IN_ERROR = "LOG_IN_ERROR";
export const ADD_USER_DEX = "ADD_USER_DEX";
export const UPDATE_USER_DEX = "UPDATE_USER_DEX";
export const UPDATE_PICTURE = "UPDATE_PICTURE";
export const UNINITIALIZE_SESSION = "UNINITIALIZE_SESSION";

interface UninitializeSessionAction {
  type: typeof UNINITIALIZE_SESSION;
}

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
  payload: UserDex;
}

interface UpdateUserDexAction {
  type: typeof UPDATE_USER_DEX;
  payload: {
    dexId: string;
    update: (dex: UserDex) => UserDex;
  };
}

interface UpdatePictureAction {
  type: typeof UPDATE_PICTURE;
  payload: string;
}

export type SessionAction =
  | InvalidateSessionAction
  | LogInAction
  | LoginErrorAction
  | AddUserDexAction
  | UpdateUserDexAction
  | UpdatePictureAction
  | UninitializeSessionAction;

export interface LoggedInState {
  token: string;
  user: User;
  picture?: string;
  type: "LOGGED_IN";
}

export interface UninitializedState {
  type: "UNINITIALIZED";
}

export interface LoginErrorState {
  type: "ERROR";
}

export type SessionState = LoggedInState | UninitializedState | LoginErrorState;
