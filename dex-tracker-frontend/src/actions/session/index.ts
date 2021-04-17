import {
  INVALIDATE_SESSION,
  SessionAction,
  LOG_IN_ACTION,
  LOG_IN_ERROR,
  UPDATE_USER_DEX,
  UPDATE_PICTURE,
  UNINITIALIZE_SESSION,
  ADD_USER_DEX,
} from "../../store/session";
import { User, UserDex } from "../../types/user";

export function uninitialize(): SessionAction {
  return {
    type: UNINITIALIZE_SESSION,
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
      type: "LOGGED_IN",
    },
  };
}

export function loginError(): SessionAction {
  return {
    type: LOG_IN_ERROR,
  };
}

export function updatePicture(picture: string): SessionAction {
  return {
    type: UPDATE_PICTURE,
    payload: picture,
  };
}

export function addUserDex(dex: UserDex): SessionAction {
  return {
    type: ADD_USER_DEX,
    payload: dex,
  };
}

export function updatePokedex(
  dexId: string,
  f: (dex: UserDex) => UserDex
): SessionAction {
  return {
    type: UPDATE_USER_DEX,
    payload: {
      dexId: dexId,
      update: f,
    },
  };
}
