import {
  ADD_TO_SYNC_QUEUE,
  CLEAR_SYNCHRONIZE_QUEUE,
  RESET_TIMEOUT,
  SyncQueueAction,
} from "../../store/syncQueue";

export function syncName(name: string): SyncQueueAction {
  return {
    type: ADD_TO_SYNC_QUEUE,
    payload: {
      type: "CHANGE_DEX_NAME",
      newName: name,
    },
  };
}

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
      type: "MARK_POKEMON",
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
