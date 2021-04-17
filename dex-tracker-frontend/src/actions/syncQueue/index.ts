import {
  UPDATE_DEX,
  CLEAR_SYNCHRONIZE_QUEUE,
  RESET_TIMEOUT,
  SyncQueueAction,
} from "../../store/syncQueue";

export function updateDexName({
  dexId,
  name,
}: {
  dexId: string;
  name: string;
}): SyncQueueAction {
  return {
    type: UPDATE_DEX,
    payload: {
      dexId,
      name,
    },
  };
}

export function caughtPokemon(
  dexId: string,
  caught: Array<number>
): SyncQueueAction {
  return {
    type: UPDATE_DEX,
    payload: {
      dexId,
      caught,
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
