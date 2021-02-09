import {
  ADD_USER_DEX,
  INVALIDATE_SESSION,
  LoggedInState,
  LOG_IN_ACTION,
  LOG_IN_ERROR,
  SessionAction,
  SessionState,
  UPDATE_CAUGHT,
  UPDATE_PICTURE,
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
      if (!state.isLoggedIn) {
        return state;
      }

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
    case UPDATE_CAUGHT: {
      if (!state.isLoggedIn) {
        return state;
      }

      return {
        ...state,
        user: {
          ...state.user,
          pokedex: state.user.pokedex.map((d) =>
            d.userDexId == action.payload.dexId
              ? { ...d, caught: action.payload.update(d.caught) }
              : d
          ),
        },
      };
    }
    case UPDATE_PICTURE: {
      if (!state.isLoggedIn) {
        return state;
      }

      return {
        ...state,
        picture: action.payload,
      };
    }
    default: {
      return state;
    }
  }
}

export default sessionReducer;
