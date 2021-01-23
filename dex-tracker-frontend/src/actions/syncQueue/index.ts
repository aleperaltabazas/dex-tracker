import {
  ADD_TO_SYNC_QUEUE,
  CLEAR_SYNCHRONIZE_QUEUE,
  RESET_TIMEOUT,
  SyncQueueAction,
} from "../../store/syncQueue";

export function addToSyncQueue(
  dexNumber: number,
  caught: boolean,
  dexId: string
): SyncQueueAction {
  return {
    type: ADD_TO_SYNC_QUEUE,
    payload: {
      number: dexNumber,
      caught: caught,
      dexId: dexId,
    },
  };
}

export function clearSynchronizeQueue(): SyncQueueAction {
  return {
    type: CLEAR_SYNCHRONIZE_QUEUE,
  };
}

export function resetTimeout(): SyncQueueAction {
  return {
    type: RESET_TIMEOUT,
  };
}
