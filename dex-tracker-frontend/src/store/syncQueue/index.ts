import { Sync } from "../../types/sync";

export const ADD_TO_SYNC_QUEUE = "ADD_TO_SYNC_QUEUE";
export const CLEAR_SYNCHRONIZE_QUEUE = "CLEAR_SYNCHRONIZE_QUEUE";
export const RESET_TIMEOUT = "RESET_TIMEOUT";

interface AddToSyncQueueAction {
  type: typeof ADD_TO_SYNC_QUEUE;
  payload: Sync;
}

interface ClearSynchronzieQueueAction {
  type: typeof CLEAR_SYNCHRONIZE_QUEUE;
}

interface ResetTimeoutAction {
  type: typeof RESET_TIMEOUT;
}

export type SyncQueueAction =
  | AddToSyncQueueAction
  | ClearSynchronzieQueueAction
  | ResetTimeoutAction;

export type SyncQueueState = {
  queue: Sync[];
  timeout: ReturnType<typeof setTimeout> | undefined;
};
