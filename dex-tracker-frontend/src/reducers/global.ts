import {
  GlobalAction,
  GlobalState,
  UPDATE_CREATE_DEX_FORM_OPEN,
  UPDATE_MENU_OPEN,
} from "../store/global";

const defaultGlobalState: GlobalState = {
  menuOpen: false,
  createDexFormOpen: false,
};

function globalReducer(state = defaultGlobalState, action: GlobalAction) {
  switch (action.type) {
    case UPDATE_MENU_OPEN: {
      return {
        ...state,
        menuOpen: action.payload,
      };
    }
    case UPDATE_CREATE_DEX_FORM_OPEN: {
      return {
        ...state,
        createDexFormOpen: action.payload,
      };
    }
    default: {
      return state;
    }
  }
}

export default globalReducer;
