import { combineReducers } from "redux";
import syncQueueReducer from "./syncQueue";

const rootReducer = combineReducers({
  syncQueue: syncQueueReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
