import React, { createContext, useContext, useReducer, ReactNode } from "react";
import { User, UserDex } from "../types/user";

// --- State types ---

export interface LoggedInState {
  type: "LOGGED_IN";
  token: string;
  user: User;
  picture?: string;
}

export interface NoLoginState {
  type: "NONE";
}

export interface UninitializedState {
  type: "UNINITIALIZED";
}

export interface LoginErrorState {
  type: "ERROR";
}

export type SessionState =
  | LoggedInState
  | UninitializedState
  | LoginErrorState
  | NoLoginState;

// --- Action types ---

type SessionAction =
  | { type: "UNINITIALIZE_SESSION" }
  | { type: "INVALIDATE_SESSION" }
  | { type: "LOG_IN_ACTION"; payload: LoggedInState }
  | { type: "LOG_IN_ERROR" }
  | { type: "ADD_USER_DEX"; payload: UserDex }
  | { type: "UPDATE_USER_DEX"; payload: { dexId: string; update: (dex: UserDex) => UserDex } }
  | { type: "UPDATE_PICTURE"; payload: string }
  | { type: "NO_LOG_IN" };

// --- Reducer ---

const defaultState: SessionState = { type: "UNINITIALIZED" };

function sessionReducer(state: SessionState, action: SessionAction): SessionState {
  switch (action.type) {
    case "UNINITIALIZE_SESSION":
      return { type: "UNINITIALIZED" };
    case "INVALIDATE_SESSION":
    case "NO_LOG_IN":
      return { type: "NONE" };
    case "LOG_IN_ACTION":
      return action.payload;
    case "LOG_IN_ERROR":
      return { type: "ERROR" };
    case "ADD_USER_DEX": {
      if (state.type !== "LOGGED_IN") return state;
      return {
        ...state,
        user: { ...state.user, pokedex: state.user.pokedex.concat(action.payload) },
      };
    }
    case "UPDATE_USER_DEX": {
      if (state.type !== "LOGGED_IN") return state;
      return {
        ...state,
        user: {
          ...state.user,
          pokedex: state.user.pokedex.map((d) =>
            d.userDexId === action.payload.dexId ? action.payload.update(d) : d
          ),
        },
      };
    }
    case "UPDATE_PICTURE": {
      if (state.type !== "LOGGED_IN") return state;
      return { ...state, picture: action.payload };
    }
    default:
      return state;
  }
}

// --- Context ---

type SessionContextValue = {
  session: SessionState;
  dispatch: React.Dispatch<SessionAction>;
};

const SessionContext = createContext<SessionContextValue | undefined>(undefined);

export function SessionProvider({ children }: { children: ReactNode }) {
  const [session, dispatch] = useReducer(sessionReducer, defaultState);
  return (
    <SessionContext.Provider value={{ session, dispatch }}>
      {children}
    </SessionContext.Provider>
  );
}

export function useSession(): SessionContextValue {
  const ctx = useContext(SessionContext);
  if (!ctx) throw new Error("useSession must be used within a SessionProvider");
  return ctx;
}
