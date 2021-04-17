import { Sync } from "../../types/sync";

export const UPDATE_DEX = "UPDATE_DEX";
export const CLEAR_SYNCHRONIZE_QUEUE = "CLEAR_SYNCHRONIZE_QUEUE";
export const RESET_TIMEOUT = "RESET_TIMEOUT";

interface UpdateDexAction {
  type: typeof UPDATE_DEX;
  payload: Sync;
}

interface ClearSynchronzieQueueAction {
  type: typeof CLEAR_SYNCHRONIZE_QUEUE;
}

interface ResetTimeoutAction {
  type: typeof RESET_TIMEOUT;
}

export type SyncQueueAction =
  | UpdateDexAction
  | ClearSynchronzieQueueAction
  | ResetTimeoutAction;

export type SyncQueueState = {
  queue: Array<Sync>;
  timeout: ReturnType<typeof setTimeout> | undefined;
};
