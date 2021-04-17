import { combineReducers } from "redux";
import globalReducer from "./global";
import pokedexReducer from "./pokedex";
import sessionReducer from "./session";
import syncQueueReducer from "./syncQueue";

const rootReducer = combineReducers({
  pokedex: pokedexReducer,
  session: sessionReducer,
  syncQueue: syncQueueReducer,
  global: globalReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
