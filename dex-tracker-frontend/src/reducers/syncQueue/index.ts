import { synchronize } from "../../functions/my-dex";
import {
  ADD_TO_SYNC_QUEUE,
  CLEAR_SYNCHRONIZE_QUEUE,
  RESET_TIMEOUT,
  SyncQueueAction,
  SyncQueueState,
} from "../../store/syncQueue";
import { Sync } from "../../types/sync";

const syncTimeout = 10; // in seconds;

const defaultSyncQueueState: SyncQueueState = {
  queue: [],
  timeout: undefined,
};

function addAndDelay(state: SyncQueueState, sync: Sync) {
  const queue = state.queue.concat(sync);

  return delaySynchroniation({ ...state, queue });
}

function delaySynchroniation(state: SyncQueueState) {
  const timeout = setTimeout(
    () => synchronize(state.queue),
    syncTimeout * 1000
  );

  return {
    ...state,
    timeout,
  };
}

function syncQueueReducer(
  state = defaultSyncQueueState,
  action: SyncQueueAction
): SyncQueueState {
  switch (action.type) {
    case ADD_TO_SYNC_QUEUE: {
      if (state.timeout != undefined) {
        clearTimeout(state.timeout);
        return addAndDelay(state, action.payload);
      } else {
        return addAndDelay(state, action.payload);
      }
    }
    case CLEAR_SYNCHRONIZE_QUEUE: {
      return {
        timeout: undefined,
        queue: [],
      };
    }
    case RESET_TIMEOUT: {
      if (state.timeout != undefined) {
        clearTimeout(state.timeout);
        return delaySynchroniation(state);
      } else {
        return delaySynchroniation(state);
      }
    }
    default: {
      return state;
    }
  }
}

export default syncQueueReducer;
