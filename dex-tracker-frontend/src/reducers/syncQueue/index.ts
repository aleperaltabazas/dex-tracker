import { synchronize } from "../../functions/my-dex";
import {
  ADD_TO_SYNC_QUEUE,
  CLEAR_SYNCHRONIZE_QUEUE,
  SyncQueueAction,
  SyncQueueState,
} from "../../store/syncQueue";
import { Sync } from "../../types/sync";

const syncTimeout = 10; // in seconds;

const defaultSyncQueueState: SyncQueueState = {
  queue: [],
  timeout: undefined,
};

function delaySynchronization(state: SyncQueueState, sync: Sync) {
  const queue = state.queue.concat(sync);
  const timeout = setTimeout(() => synchronize(queue), syncTimeout * 1000);

  return {
    ...state,
    timeout: timeout,
    queue: queue,
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
        return delaySynchronization(state, action.payload);
      } else {
        return delaySynchronization(state, action.payload);
      }
    }
    case CLEAR_SYNCHRONIZE_QUEUE: {
      return {
        timeout: undefined,
        queue: [],
      };
    }
    default: {
      return state;
    }
  }
}

export default syncQueueReducer;
