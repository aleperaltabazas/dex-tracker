import { combineReducers } from "redux";
import pokedexReducer from "./pokedex";
import sessionReducer from "./session";
import syncQueueReducer from "./syncQueue";

const rootReducer = combineReducers({
  pokedex: pokedexReducer,
  session: sessionReducer,
  syncQueue: syncQueueReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
