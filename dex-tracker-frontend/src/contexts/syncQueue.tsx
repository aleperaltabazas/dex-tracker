import React, {
  createContext,
  useContext,
  useReducer,
  useRef,
  useCallback,
  ReactNode,
} from "react";
import { Sync } from "../types/sync";
import { fireSynchronize } from "../functions/my-dex";

const SYNC_DELAY_MS = 10_000;

// --- State types ---

export type SyncQueueState = {
  queue: Sync[];
  timeout: ReturnType<typeof setTimeout> | undefined;
};

// --- Action types ---

export type SyncQueueAction =
  | { type: "UPDATE_DEX"; payload: Sync }
  | { type: "CLEAR_SYNCHRONIZE_QUEUE" }
  | { type: "RESET_TIMEOUT" }
  | { type: "_SET_TIMEOUT"; payload: ReturnType<typeof setTimeout> };

// --- Reducer (pure — no side effects) ---

const defaultState: SyncQueueState = { queue: [], timeout: undefined };

function syncQueueReducer(state: SyncQueueState, action: SyncQueueAction): SyncQueueState {
  switch (action.type) {
    case "UPDATE_DEX":
      return { ...state, queue: state.queue.concat(action.payload) };
    case "CLEAR_SYNCHRONIZE_QUEUE":
      return { queue: [], timeout: undefined };
    case "RESET_TIMEOUT":
    case "_SET_TIMEOUT":
      return { ...state, timeout: action.payload };
    default:
      return state;
  }
}

// --- Context ---

type SyncQueueContextValue = {
  syncQueue: SyncQueueState;
  dispatch: (action: SyncQueueAction, userId?: string) => void;
};

const SyncQueueContext = createContext<SyncQueueContextValue | undefined>(undefined);

export function SyncQueueProvider({ children }: { children: ReactNode }) {
  const [syncQueue, rawDispatch] = useReducer(syncQueueReducer, defaultState);
  const stateRef = useRef(syncQueue);
  stateRef.current = syncQueue;

  const scheduleSync = useCallback((userId: string) => {
    const newTimeout = setTimeout(async () => {
      if (stateRef.current.queue.length === 0) return;
      try {
        await fireSynchronize(userId, stateRef.current.queue);
        rawDispatch({ type: "CLEAR_SYNCHRONIZE_QUEUE" });
      } catch {
        const retryTimeout = setTimeout(() => scheduleSync(userId), SYNC_DELAY_MS);
        rawDispatch({ type: "_SET_TIMEOUT", payload: retryTimeout });
      }
    }, SYNC_DELAY_MS);
    rawDispatch({ type: "_SET_TIMEOUT", payload: newTimeout });
  }, []);

  const dispatch = useCallback(
    (action: SyncQueueAction, userId?: string) => {
      if (
        (action.type === "UPDATE_DEX" || action.type === "RESET_TIMEOUT") &&
        stateRef.current.timeout !== undefined
      ) {
        clearTimeout(stateRef.current.timeout);
      }

      rawDispatch(action);

      if ((action.type === "UPDATE_DEX" || action.type === "RESET_TIMEOUT") && userId) {
        scheduleSync(userId);
      }
    },
    [scheduleSync]
  );

  return (
    <SyncQueueContext.Provider value={{ syncQueue, dispatch }}>
      {children}
    </SyncQueueContext.Provider>
  );
}

export function useSyncQueue(): SyncQueueContextValue {
  const ctx = useContext(SyncQueueContext);
  if (!ctx) throw new Error("useSyncQueue must be used within a SyncQueueProvider");
  return ctx;
}
