import {
  ADD_TO_SYNC_QUEUE,
  CLEAR_SYNCHRONIZE_QUEUE,
  SyncQueueAction,
} from "../../store/syncQueue";

export function addToSyncQueue(
  dexNumber: number,
  caught: boolean
): SyncQueueAction {
  return {
    type: ADD_TO_SYNC_QUEUE,
    payload: {
      number: dexNumber,
      caught: caught,
    },
  };
}

export function clearSynchronizeQueue(): SyncQueueAction {
  return {
    type: CLEAR_SYNCHRONIZE_QUEUE,
  };
}
