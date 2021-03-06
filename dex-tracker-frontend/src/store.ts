import { compose, createStore } from "redux";
import rootReducer, { RootState } from "./reducers";

const defaultRootState: RootState = {
  session: {
    type: "UNINITIALIZED",
  },
  syncQueue: {
    timeout: undefined,
    queue: [],
  },
  pokedex: {
    loaded: false,
  },
  global: {
    menuOpen: false,
    createDexFormOpen: false,
  },
};

const store = createStore(
  rootReducer,
  defaultRootState,
  ((window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose)()
);

export default store;
