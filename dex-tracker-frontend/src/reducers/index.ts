import { combineReducers } from "redux";
import gamesReducer from "./games";
import pokedexReducer from "./pokedex";
import sessionReducer from "./session";
import syncQueueReducer from "./syncQueue";

const rootReducer = combineReducers({
  pokedex: pokedexReducer,
  session: sessionReducer,
  syncQueue: syncQueueReducer,
  games: gamesReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
