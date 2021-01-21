import {
  INVALIDATE_SESSION,
  LOG_IN_ACTION,
  LOG_IN_ERROR,
  SessionAction,
  SessionState,
} from "../../store/session";

const defaultSessionState: SessionState = {
  isLoggedIn: false,
  isError: false,
};

function sessionReducer(
  state = defaultSessionState,
  action: SessionAction
): SessionState {
  switch (action.type) {
    case INVALIDATE_SESSION: {
      return {
        isLoggedIn: false,
        isError: false,
      };
    }
    case LOG_IN_ACTION: {
      return action.payload;
    }
    case LOG_IN_ERROR: {
      return {
        isError: true,
        isLoggedIn: false,
      };
    }
    default: {
      return state;
    }
  }
}

export default sessionReducer;
