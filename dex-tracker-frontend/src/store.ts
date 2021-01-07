import { compose, createStore } from "redux";
import rootReducer, { RootState } from "./reducers";

const defaultRootState: RootState = {
  session: {
    isLoggedIn: false,
  },
  syncQueue: {
    timeout: undefined,
    queue: [],
  },
};

const store = createStore(
  rootReducer,
  defaultRootState,
  ((window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose)()
);

export default store;
