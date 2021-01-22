import {
  INVALIDATE_SESSION,
  SessionAction,
  LOG_IN_ACTION,
  LOG_IN_ERROR,
  ADD_USER_DEX,
} from "../../store/session";
import { User, UserDex } from "../../types/user";

export function addUserDex(dex: UserDex): SessionAction {
  return {
    type: ADD_USER_DEX,
    payload: dex,
  };
}

export function invalidateSession(): SessionAction {
  return {
    type: INVALIDATE_SESSION,
  };
}

export function updateSessionState(token: string, user: User): SessionAction {
  return {
    type: LOG_IN_ACTION,
    payload: {
      token,
      user,
      isLoggedIn: true,
      isError: false,
    },
  };
}

export function loginError(): SessionAction {
  return {
    type: LOG_IN_ERROR,
  };
}
