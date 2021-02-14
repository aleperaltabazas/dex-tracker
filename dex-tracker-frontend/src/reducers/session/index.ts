import { readLocalPokedex } from "../../functions/storage";
import {
  ADD_USER_DEX,
  INVALIDATE_SESSION,
  LoggedInState,
  LOG_IN_ACTION,
  LOG_IN_ERROR,
  NOT_LOGGED_IN,
  SessionAction,
  SessionState,
  UNINITIALIZE_SESSION,
  UPDATE_USER_DEX,
  UPDATE_PICTURE,
} from "../../store/session";

const defaultSessionState: SessionState = {
  type: "UNINITIALIZED",
};

function sessionReducer(
  state = defaultSessionState,
  action: SessionAction
): SessionState {
  switch (action.type) {
    case INVALIDATE_SESSION: {
      return {
        type: "NOT_LOGGED_IN",
        localDex: readLocalPokedex(),
      };
    }
    case LOG_IN_ACTION: {
      return action.payload;
    }
    case LOG_IN_ERROR: {
      return {
        type: "ERROR",
      };
    }
    case ADD_USER_DEX: {
      switch (state.type) {
        case "LOGGED_IN": {
          const newState: LoggedInState = {
            ...state,
            user: {
              ...state.user,
              pokedex: state.user.pokedex.concat(action.payload),
            },
          };

          return newState;
        }
        case "NOT_LOGGED_IN": {
          return {
            ...state,
            localDex: state.localDex.concat(action.payload),
          };
        }
        default:
          return state;
      }
    }
    case UNINITIALIZE_SESSION: {
      return {
        type: "UNINITIALIZED",
      };
    }
    case UPDATE_USER_DEX: {
      switch (state.type) {
        case "LOGGED_IN": {
          return {
            ...state,
            user: {
              ...state.user,
              pokedex: state.user.pokedex.map((d) =>
                d.userDexId == action.payload.dexId
                  ? action.payload.update(d)
                  : d
              ),
            },
          };
        }
        case "NOT_LOGGED_IN": {
          return {
            ...state,
            localDex: state.localDex.map((d) =>
              d.userDexId == action.payload.dexId ? action.payload.update(d) : d
            ),
          };
        }
        default:
          return state;
      }
    }
    case UPDATE_PICTURE: {
      if (state.type == "LOGGED_IN") {
        return {
          ...state,
          picture: action.payload,
        };
      }

      return state;
    }
    case NOT_LOGGED_IN: {
      return {
        type: NOT_LOGGED_IN,
        localDex: readLocalPokedex(),
      };
    }
    default: {
      return state;
    }
  }
}

export default sessionReducer;
