import {
  INVALIDATE_SESSION,
  SessionAction,
  LOG_IN_ACTION,
} from "../../store/session";

export function invalidateSession(): SessionAction {
  return {
    type: INVALIDATE_SESSION,
  };
}

export function updateSessionState(token: string): SessionAction {
  return {
    type: LOG_IN_ACTION,
    payload: {
      token,
      isLoggedIn: true,
    },
  };
}
