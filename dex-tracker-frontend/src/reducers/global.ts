import { GlobalAction, GlobalState, UPDATE_MENU_OPEN } from "../store/global";

const defaultGlobalState: GlobalState = {
  menuOpen: false,
};

function globalReducer(state = defaultGlobalState, action: GlobalAction) {
  switch (action.type) {
    case UPDATE_MENU_OPEN: {
      return {
        ...state,
        menuOpen: action.payload,
      };
    }
    default: {
      return state;
    }
  }
}

export default globalReducer;
