import {
  INVALIDATE_SESSION,
  SessionAction,
  LOG_IN_ACTION,
  LOG_IN_ERROR,
  ADD_USER_DEX,
  UPDATE_CAUGHT,
  UPDATE_PICTURE,
  NOT_LOGGED_IN,
  UNINITIALIZE_SESSION,
} from "../../store/session";
import { User, UserDexRef } from "../../types/user";

export function addUserDex(dex: UserDexRef): SessionAction {
  return {
    type: ADD_USER_DEX,
    payload: dex,
  };
}

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

export function notLoggedIn(): SessionAction {
  return {
    type: NOT_LOGGED_IN,
  };
}

export function loginError(): SessionAction {
  return {
    type: LOG_IN_ERROR,
  };
}

const updateCaught: (
  update: (current: number) => number
) => (dexId: string) => SessionAction = (
  update: (current: number) => number
) => (dexId: string) => ({ type: UPDATE_CAUGHT, payload: { dexId, update } });

export const incrementCaught: (dexId: string) => SessionAction = updateCaught(
  (n) => n + 1
);

export const decrementCaught: (dexId: string) => SessionAction = updateCaught(
  (n) => n - 1
);

export function updatePicture(picture: string): SessionAction {
  return {
    type: UPDATE_PICTURE,
    payload: picture,
  };
}
