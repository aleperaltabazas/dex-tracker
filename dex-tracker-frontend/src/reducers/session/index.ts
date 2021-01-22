import {
  ADD_USER_DEX,
  INVALIDATE_SESSION,
  LoggedInState,
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
    case ADD_USER_DEX: {
      const loggedIn = state as LoggedInState;
      const user = loggedIn.user;

      const newState: LoggedInState = {
        ...loggedIn,
        user: {
          ...user,
          pokedex: user.pokedex.concat(action.payload),
        },
      };

      return newState;
    }
    default: {
      return state;
    }
  }
}

export default sessionReducer;
