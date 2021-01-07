import {
  INVALIDATE_SESSION,
  LOG_IN_ACTION,
  SessionAction,
  SessionState,
} from "../../store/session";

const defaultSessionState: SessionState = {
  isLoggedIn: false,
};

function sessionReducer(
  state = defaultSessionState,
  action: SessionAction
): SessionState {
  switch (action.type) {
    case INVALIDATE_SESSION: {
      return {
        isLoggedIn: false,
      };
    }
    case LOG_IN_ACTION: {
      return action.payload;
    }
    default: {
      return state;
    }
  }
}

export default sessionReducer;
