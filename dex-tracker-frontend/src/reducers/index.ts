import { combineReducers } from "redux";
import sessionReducer from "./session";
import syncQueueReducer from "./syncQueue";

const rootReducer = combineReducers({
  session: sessionReducer,
  syncQueue: syncQueueReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
